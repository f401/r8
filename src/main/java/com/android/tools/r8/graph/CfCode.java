// Copyright (c) 2017, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.graph;

import com.android.tools.r8.cf.CfPrinter;
import com.android.tools.r8.cf.code.CfFrame;
import com.android.tools.r8.cf.code.CfInstruction;
import com.android.tools.r8.cf.code.CfLabel;
import com.android.tools.r8.cf.code.CfPosition;
import com.android.tools.r8.cf.code.CfReturnVoid;
import com.android.tools.r8.cf.code.CfTryCatch;
import com.android.tools.r8.errors.Unimplemented;
import com.android.tools.r8.ir.code.IRCode;
import com.android.tools.r8.ir.code.Position;
import com.android.tools.r8.ir.code.ValueNumberGenerator;
import com.android.tools.r8.ir.conversion.CfSourceCode;
import com.android.tools.r8.ir.conversion.IRBuilder;
import com.android.tools.r8.naming.ClassNameMapper;
import com.android.tools.r8.naming.NamingLens;
import com.android.tools.r8.origin.Origin;
import com.android.tools.r8.utils.InternalOptions;
import it.unimi.dsi.fastutil.ints.Int2ReferenceArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class CfCode extends Code {

  public static class LocalVariableInfo {
    private final int index;
    private final DebugLocalInfo local;
    private final CfLabel start;
    private CfLabel end;

    public LocalVariableInfo(int index, DebugLocalInfo local, CfLabel start) {
      this.index = index;
      this.local = local;
      this.start = start;
    }

    public LocalVariableInfo(int index, DebugLocalInfo local, CfLabel start, CfLabel end) {
      this(index, local, start);
      setEnd(end);
    }

    public void setEnd(CfLabel end) {
      assert this.end == null;
      assert end != null;
      this.end = end;
    }

    public int getIndex() {
      return index;
    }

    public DebugLocalInfo getLocal() {
      return local;
    }

    public CfLabel getStart() {
      return start;
    }

    public CfLabel getEnd() {
      return end;
    }

    @Override
    public String toString() {
      return "" + index + " => " + local;
    }
  }

  private final int maxStack;
  private final int maxLocals;
  public final List<CfInstruction> instructions;
  private final List<CfTryCatch> tryCatchRanges;
  private final List<LocalVariableInfo> localVariables;

  public CfCode(
      int maxStack,
      int maxLocals,
      List<CfInstruction> instructions,
      List<CfTryCatch> tryCatchRanges,
      List<LocalVariableInfo> localVariables) {
    this.maxStack = maxStack;
    this.maxLocals = maxLocals;
    this.instructions = instructions;
    this.tryCatchRanges = tryCatchRanges;
    this.localVariables = localVariables;
  }

  public int getMaxStack() {
    return maxStack;
  }

  public int getMaxLocals() {
    return maxLocals;
  }

  public List<CfTryCatch> getTryCatchRanges() {
    return tryCatchRanges;
  }

  public List<CfInstruction> getInstructions() {
    return Collections.unmodifiableList(instructions);
  }

  public List<LocalVariableInfo> getLocalVariables() {
    return Collections.unmodifiableList(localVariables);
  }

  @Override
  public int estimatedSizeForInlining() {
    return countNonStackOperations(Integer.MAX_VALUE);
  }

  @Override
  public boolean estimatedSizeForInliningAtMost(int threshold) {
    return countNonStackOperations(threshold) <= threshold;
  }

  private int countNonStackOperations(int threshold) {
    int result = 0;
    for (CfInstruction instruction : instructions) {
      if (instruction.emitsIR()) {
        result++;
        if (result > threshold) {
          break;
        }
      }
    }
    return result;
  }

  @Override
  public boolean isCfCode() {
    return true;
  }

  @Override
  public CfCode asCfCode() {
    return this;
  }

  public void write(
      MethodVisitor visitor, NamingLens namingLens, InternalOptions options, int classFileVersion) {
    for (CfInstruction instruction : instructions) {
      if (instruction instanceof CfFrame
          && (classFileVersion <= 49
              || (classFileVersion == 50
                  && !options.getProguardConfiguration().getKeepAttributes().stackMapTable))) {
        continue;
      }
      instruction.write(visitor, namingLens);
    }
    visitor.visitEnd();
    visitor.visitMaxs(maxStack, maxLocals);
    for (CfTryCatch tryCatch : tryCatchRanges) {
      Label start = tryCatch.start.getLabel();
      Label end = tryCatch.end.getLabel();
      for (int i = 0; i < tryCatch.guards.size(); i++) {
        DexType guard = tryCatch.guards.get(i);
        Label target = tryCatch.targets.get(i).getLabel();
        visitor.visitTryCatchBlock(
            start,
            end,
            target,
            guard == options.itemFactory.throwableType
                ? null
                : namingLens.lookupInternalName(guard));
      }
    }
    for (LocalVariableInfo localVariable : localVariables) {
      DebugLocalInfo info = localVariable.local;
      visitor.visitLocalVariable(
          info.name.toString(),
          namingLens.lookupDescriptor(info.type).toString(),
          info.signature == null ? null : info.signature.toString(),
          localVariable.start.getLabel(),
          localVariable.end.getLabel(),
          localVariable.index);
    }
  }

  @Override
  protected int computeHashCode() {
    throw new Unimplemented();
  }

  @Override
  protected boolean computeEquals(Object other) {
    throw new Unimplemented();
  }

  @Override
  public boolean isEmptyVoidMethod() {
    for (CfInstruction insn : instructions) {
      if (!(insn instanceof CfReturnVoid)
          && !(insn instanceof CfLabel)
          && !(insn instanceof CfPosition)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public IRCode buildIR(DexEncodedMethod encodedMethod, AppView<?> appView, Origin origin) {
    return internalBuild(encodedMethod, encodedMethod, appView, null, null, origin);
  }

  @Override
  public IRCode buildInliningIR(
      DexEncodedMethod context,
      DexEncodedMethod encodedMethod,
      AppView<?> appView,
      ValueNumberGenerator valueNumberGenerator,
      Position callerPosition,
      Origin origin) {
    assert valueNumberGenerator != null;
    assert callerPosition != null;
    return internalBuild(
        context, encodedMethod, appView, valueNumberGenerator, callerPosition, origin);
  }

  private IRCode internalBuild(
      DexEncodedMethod context,
      DexEncodedMethod encodedMethod,
      AppView<?> appView,
      ValueNumberGenerator generator,
      Position callerPosition,
      Origin origin) {
    CfSourceCode source =
        new CfSourceCode(
            this,
            encodedMethod,
            appView.graphLense().getOriginalMethodSignature(encodedMethod.method),
            callerPosition,
            origin,
            appView);
    if (!keepLocals(encodedMethod, appView.options())) {
      // If the locals are not kept, we might still need a bit of locals information to satisfy
      // -keepparameternames for R8. As locals are stripped after collecting the parameter names
      // this information can only be retrieved the first time IR is build for a method, so stick
      // to the information if already present.
      if (!encodedMethod.hasParameterInfo()) {
        encodedMethod.setParameterInfo(collectParameterInfo(encodedMethod, appView));
      }
    }
    return new IRBuilder(encodedMethod, appView, source, origin, generator).build(context);
  }

  @Override
  public void registerCodeReferences(DexEncodedMethod method, UseRegistry registry) {
    for (CfInstruction instruction : instructions) {
      instruction.registerUse(registry, method.method.holder);
    }
    for (CfTryCatch tryCatch : tryCatchRanges) {
      for (DexType guard : tryCatch.guards) {
        registry.registerTypeReference(guard);
      }
    }
  }

  private boolean keepLocals(DexEncodedMethod encodedMethod, InternalOptions options) {
    if (options.testing.noLocalsTableOnInput) {
      return false;
    }
    if (options.debug || encodedMethod.getOptimizationInfo().isReachabilitySensitive()) {
      return true;
    }
    return false;
  }

  private Int2ReferenceMap<DebugLocalInfo> collectParameterInfo(
      DexEncodedMethod encodedMethod, AppView<?> appView) {
    CfLabel firstLabel = null;
    for (CfInstruction instruction : instructions) {
      if (instruction instanceof CfLabel) {
        firstLabel = (CfLabel) instruction;
        break;
      }
    }
    if (firstLabel == null) {
      return DexEncodedMethod.NO_PARAMETER_INFO;
    }
    if (!appView.options().hasProguardConfiguration()
        || !appView.options().getProguardConfiguration().isKeepParameterNames()) {
      return DexEncodedMethod.NO_PARAMETER_INFO;
    }
    // The enqueuer might build IR to trace reflective behaviour. At that point liveness is not
    // known, so be conservative with collection parameter name information.
    if (appView.appInfo().hasLiveness()
        && !appView.appInfo().withLiveness().isPinned(encodedMethod.method)) {
      return DexEncodedMethod.NO_PARAMETER_INFO;
    }

    // Collect the local slots used for parameters.
    BitSet localSlotsForParameters = new BitSet(0);
    int nextLocalSlotsForParameters = 0;
    if (!encodedMethod.isStatic()) {
      localSlotsForParameters.set(nextLocalSlotsForParameters++);
    }
    for (DexType type : encodedMethod.method.proto.parameters.values) {
      localSlotsForParameters.set(nextLocalSlotsForParameters);
      nextLocalSlotsForParameters += type.isLongType() || type.isDoubleType() ? 2 : 1;
    }
    // Collect the first piece of local variable information for each argument local slot,
    // assuming that that does actually describe the parameter (name, type and possibly
    // signature).
    Int2ReferenceMap<DebugLocalInfo> parameterInfo =
        new Int2ReferenceArrayMap<>(localSlotsForParameters.cardinality());
    for (LocalVariableInfo node : localVariables) {
      if (node.start == firstLabel
          && node.index < nextLocalSlotsForParameters
          && localSlotsForParameters.get(node.index)
          && !parameterInfo.containsKey(node.index)) {
        parameterInfo.put(
            node.index, new DebugLocalInfo(node.local.name, node.local.type, node.local.signature));
      }
    }
    return parameterInfo;
  }

  @Override
  public String toString() {
    return new CfPrinter(this).toString();
  }

  @Override
  public String toString(DexEncodedMethod method, ClassNameMapper naming) {
    return new CfPrinter(this, method, naming).toString();
  }
}
