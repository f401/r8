// Copyright (c) 2022, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.ir.optimize;

import com.android.tools.r8.contexts.CompilationContext.MethodProcessingContext;
import com.android.tools.r8.graph.AppView;
import com.android.tools.r8.graph.DexItemFactory;
import com.android.tools.r8.graph.DexMethod;
import com.android.tools.r8.graph.DexProto;
import com.android.tools.r8.graph.MethodAccessFlags;
import com.android.tools.r8.graph.ProgramMethod;
import com.android.tools.r8.ir.analysis.type.DynamicType;
import com.android.tools.r8.ir.analysis.type.Nullability;
import com.android.tools.r8.ir.analysis.type.TypeAnalysis;
import com.android.tools.r8.ir.code.BasicBlock;
import com.android.tools.r8.ir.code.IRCode;
import com.android.tools.r8.ir.code.Instruction;
import com.android.tools.r8.ir.code.InstructionListIterator;
import com.android.tools.r8.ir.code.InvokeStatic;
import com.android.tools.r8.ir.code.NewInstance;
import com.android.tools.r8.ir.code.Value;
import com.android.tools.r8.ir.desugar.backports.BackportedMethods;
import com.android.tools.r8.ir.optimize.info.OptimizationFeedback;
import com.android.tools.r8.utils.InternalOptions;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class AssertionErrorTwoArgsConstructorRewriter {

  private final AppView<?> appView;
  private final DexItemFactory dexItemFactory;
  private final InternalOptions options;

  public AssertionErrorTwoArgsConstructorRewriter(AppView<?> appView) {
    this.appView = appView;
    this.options = appView.options();
    this.dexItemFactory = appView.dexItemFactory();
  }

  public void rewrite(IRCode code, MethodProcessingContext methodProcessingContext) {
    if (options.canUseAssertionErrorTwoArgumentConstructor()) {
      return;
    }
    Set<Value> newOutValues = Sets.newIdentityHashSet();
    ListIterator<BasicBlock> blockIterator = code.listIterator();
    while (blockIterator.hasNext()) {
      BasicBlock block = blockIterator.next();
      InstructionListIterator insnIterator = block.listIterator(code);
      List<NewInstance> newInstancesToRemove = new ArrayList<>();
      while (insnIterator.hasNext()) {
        Instruction current = insnIterator.next();
        if (current.isInvokeMethod()) {
          DexMethod invokedMethod = current.asInvokeMethod().getInvokedMethod();
          if (invokedMethod == dexItemFactory.assertionErrorMethods.initMessageAndCause) {
            List<Value> inValues = current.inValues();
            assert inValues.size() == 3; // receiver, message, cause
            Value newInstanceValue = current.getFirstOperand();
            Instruction definition = newInstanceValue.getDefinition();
            if (!definition.isNewInstance()) {
              continue;
            }
            InvokeStatic invoke =
                InvokeStatic.builder()
                    .setMethod(createSynthetic(methodProcessingContext).getReference())
                    .setFreshOutValue(
                        code, dexItemFactory.assertionErrorType.toTypeElement(appView))
                    .setPosition(current)
                    .setArguments(inValues.subList(1, 3))
                    .build();
            insnIterator.replaceCurrentInstruction(invoke);
            newInstanceValue.replaceUsers(invoke.outValue());
            newOutValues.add(invoke.outValue());
            newInstancesToRemove.add(definition.asNewInstance());
          }
        }
      }
      newInstancesToRemove.forEach(
          newInstance -> newInstance.removeOrReplaceByDebugLocalRead(code));
    }
    if (!newOutValues.isEmpty()) {
      new TypeAnalysis(appView).widening(newOutValues);
    }
    assert code.isConsistentSSA(appView);
  }

  private final List<ProgramMethod> synthesizedMethods = new ArrayList<>();

  public List<ProgramMethod> getSynthesizedMethods() {
    return synthesizedMethods;
  }

  private ProgramMethod createSynthetic(MethodProcessingContext methodProcessingContext) {
    DexItemFactory factory = appView.dexItemFactory();
    DexProto proto =
        factory.createProto(factory.assertionErrorType, factory.stringType, factory.throwableType);
    ProgramMethod method =
        appView
            .getSyntheticItems()
            .createMethod(
                kinds -> kinds.BACKPORT,
                methodProcessingContext.createUniqueContext(),
                appView,
                builder ->
                    builder
                        .setApiLevelForCode(appView.computedMinApiLevel())
                        .setProto(proto)
                        .setAccessFlags(MethodAccessFlags.createPublicStaticSynthetic())
                        .setCode(
                            methodSig ->
                                BackportedMethods.AssertionErrorMethods_createAssertionError(
                                    factory, methodSig)));
    synchronized (synthesizedMethods) {
      synthesizedMethods.add(method);
      OptimizationFeedback.getSimpleFeedback()
          .setDynamicReturnType(
              method,
              appView,
              DynamicType.createExact(
                  dexItemFactory
                      .assertionErrorType
                      .toTypeElement(appView, Nullability.definitelyNotNull())
                      .asClassType()));
    }
    return method;
  }
}
