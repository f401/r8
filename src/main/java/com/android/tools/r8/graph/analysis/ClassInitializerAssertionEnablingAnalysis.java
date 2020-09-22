// Copyright (c) 2019, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.graph.analysis;

import com.android.tools.r8.cf.code.CfConstNumber;
import com.android.tools.r8.cf.code.CfFieldInstruction;
import com.android.tools.r8.cf.code.CfGoto;
import com.android.tools.r8.cf.code.CfIf;
import com.android.tools.r8.cf.code.CfInstruction;
import com.android.tools.r8.cf.code.CfInvoke;
import com.android.tools.r8.cf.code.CfLogicalBinop;
import com.android.tools.r8.graph.CfCode;
import com.android.tools.r8.graph.Code;
import com.android.tools.r8.graph.DexEncodedMethod;
import com.android.tools.r8.graph.DexItemFactory;
import com.android.tools.r8.graph.DexString;
import com.android.tools.r8.graph.ProgramMethod;
import com.android.tools.r8.ir.optimize.info.OptimizationFeedback;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.objectweb.asm.Opcodes;

public class ClassInitializerAssertionEnablingAnalysis extends EnqueuerAnalysis {
  private final DexItemFactory dexItemFactory;
  private final OptimizationFeedback feedback;
  private final DexString kotlinAssertionsEnabled;

  public ClassInitializerAssertionEnablingAnalysis(
      DexItemFactory dexItemFactory, OptimizationFeedback feedback) {
    this.dexItemFactory = dexItemFactory;
    this.feedback = feedback;
    this.kotlinAssertionsEnabled = dexItemFactory.createString("ENABLED");
  }

  @Override
  public void processNewlyLiveMethod(ProgramMethod method) {
    DexEncodedMethod definition = method.getDefinition();
    if (definition.isClassInitializer()) {
      Code code = definition.getCode();
      if (code.isCfCode()) {
        if (hasJavacClinitAssertionCode(code.asCfCode()) || hasKotlincClinitAssertionCode(method)) {
          feedback.setInitializerEnablingJavaVmAssertions(definition);
        }
      }
    }
  }

  // The <clinit> instruction sequence generated by javac for classes which use assertions. The
  // call to desiredAssertionStatus() will provide the value for the -ea option for the class.
  //
  //  0: ldc           #<n>                // Current class
  //  2: invokevirtual #<n>                // Method java/lang/Class.desiredAssertionStatus:()Z
  //  5: ifne          12
  //  8: iconst_1
  //  9: goto          13
  // 12: iconst_0
  // 13: putstatic     #<n>                // Field $assertionsDisabled:Z

  // R8 processing with class file backend will rewrite this sequence to either of the following
  // depending on debug or release mode.

  //  2: ldc           #<n>                // Current class
  //  3: invokevirtual #<n>                // Method java/lang/Class.desiredAssertionStatus:()Z
  //  4: istore        0
  //  5: iload         0
  //  6: ldc           1
  //  7: ixor
  //  8: istore        0
  //  9: iload         0
  // 13: putstatic     #<n>                // Field $assertionsDisabled:Z

  //  2: ldc           #<n>                // Current class
  //  3: invokevirtual                     // Method java/lang/Class.desiredAssertionStatus:()Z
  //  4: ldc           1
  //  5: ixor
  // 13: putstatic     #<n>                // Field $assertionsDisabled:Z

  // The <clinit> instruction sequence generated by kolinc for kotlin._Assertions.
  //
  //  0: new           #2                  // class kotlin/_Assertions
  //  3: dup
  //  4: invokespecial #31                 // Method "<init>":()V
  //  7: astore_0
  //  8: aload_0
  //  9: putstatic     #33                 // Field INSTANCE:Lkotlin/_Assertions;
  // 12: aload_0
  // 13: invokevirtual #37                 // Method java/lang/Object.getClass:()Ljava/lang/Class;
  // 16: invokevirtual #43                 // Method java/lang/Class.desiredAssertionStatus:()Z
  // 19: putstatic     #45                 // Field ENABLED:Z
  // 22: return

  private static List<Class<?>> javacInstructionSequence =
      ImmutableList.of(
          CfIf.class,
          CfConstNumber.class,
          CfGoto.class,
          CfConstNumber.class,
          CfFieldInstruction.class);
  private static List<Class<?>> r8InstructionSequence =
      ImmutableList.of(CfConstNumber.class, CfLogicalBinop.class, CfFieldInstruction.class);

  private boolean hasJavacClinitAssertionCode(CfCode code) {
    for (int i = 0; i < code.getInstructions().size(); i++) {
      CfInstruction instruction = code.getInstructions().get(i);
      if (instruction.isInvoke()) {
        // Check for the generated instruction sequence by looking for the call to
        // desiredAssertionStatus() followed by the expected instruction types and finally checking
        // the exact target of the putstatic ending the sequence.
        CfInvoke invoke = instruction.asInvoke();
        if (invoke.getOpcode() == Opcodes.INVOKEVIRTUAL
            && invoke.getMethod() == dexItemFactory.classMethods.desiredAssertionStatus) {
          CfFieldInstruction fieldInstruction = isJavacInstructionSequence(code, i + 1);
          if (fieldInstruction == null) {
            fieldInstruction = isR8InstructionSequence(code, i + 1);
          }
          if (fieldInstruction != null) {
            return fieldInstruction.getOpcode() == Opcodes.PUTSTATIC
                && fieldInstruction.getField().name == dexItemFactory.assertionsDisabled;
          }
        }
      }
      // Only check initial straight line code.
      if (instruction.isJump()) {
        return false;
      }
    }
    return false;
  }

  private boolean hasKotlincClinitAssertionCode(ProgramMethod method) {
    if (method.getHolderType() == dexItemFactory.kotlin.assertions.type) {
      CfCode code = method.getDefinition().getCode().asCfCode();
      List<CfInstruction> instructions = code.getInstructions();
      for (int i = 1; i < instructions.size(); i++) {
        CfInstruction instruction = instructions.get(i - 1);
        if (instruction.isInvoke()) {
          // Check for the generated instruction sequence by looking for the call to
          // desiredAssertionStatus() followed by the expected instruction types and finally
          // checking the exact target of the putstatic ending the sequence.
          CfInvoke invoke = instruction.asInvoke();
          if (invoke.getOpcode() == Opcodes.INVOKEVIRTUAL
              && invoke.getMethod() == dexItemFactory.classMethods.desiredAssertionStatus) {
            if (instructions.get(i).isFieldInstruction()) {
              CfFieldInstruction fieldInstruction = instructions.get(i).asFieldInstruction();
              if (fieldInstruction.getOpcode() == Opcodes.PUTSTATIC
                  && fieldInstruction.getField().name == kotlinAssertionsEnabled) {
                return true;
              }
            }
          }
        }
        // Only check initial straight line code.
        if (instruction.isJump()) {
          return false;
        }
      }
    }
    return false;
  }

  private CfFieldInstruction isJavacInstructionSequence(CfCode code, int fromIndex) {
    List<Class<?>> sequence = javacInstructionSequence;
    int nextExpectedInstructionIndex = 0;
    CfInstruction instruction = null;
    for (int i = fromIndex;
        i < code.getInstructions().size() && nextExpectedInstructionIndex < sequence.size();
        i++) {
      instruction = code.getInstructions().get(i);
      if (instruction.isLabel() || instruction.isFrame()) {
        // Just ignore labels and frames.
        continue;
      }
      if (instruction.getClass() != sequence.get(nextExpectedInstructionIndex)) {
        break;
      }
      nextExpectedInstructionIndex++;
    }
    return nextExpectedInstructionIndex == sequence.size()
        ? instruction.asFieldInstruction()
        : null;
  }

  private CfFieldInstruction isR8InstructionSequence(CfCode code, int fromIndex) {
    List<Class<?>> sequence = r8InstructionSequence;
    int nextExpectedInstructionIndex = 0;
    CfInstruction instruction = null;
    for (int i = fromIndex;
        i < code.getInstructions().size() && nextExpectedInstructionIndex < sequence.size();
        i++) {
      instruction = code.getInstructions().get(i);
      if (instruction.isStore() || instruction.isLoad()) {
        // Just ignore stores and loads.
        continue;
      }
      if (instruction.getClass() != sequence.get(nextExpectedInstructionIndex)) {
        break;
      }
      nextExpectedInstructionIndex++;
    }
    return nextExpectedInstructionIndex == sequence.size()
        ? instruction.asFieldInstruction()
        : null;
  }
}
