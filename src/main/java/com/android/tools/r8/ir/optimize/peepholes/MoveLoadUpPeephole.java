// Copyright (c) 2018, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.ir.optimize.peepholes;

import com.android.tools.r8.ir.code.Instruction;
import com.android.tools.r8.ir.code.InstructionListIterator;
import com.android.tools.r8.ir.code.Load;
import com.android.tools.r8.ir.code.StackValue;
import com.android.tools.r8.ir.code.Value;

/**
 * {@link MoveLoadUpPeephole} looks for the following pattern:
 *
 * <pre>
 * ...                  (no clobbering of v0 and same stack height)
 * Load                 s0 <- v0
 * </pre>
 *
 * and replaces with:
 *
 * <pre>
 * Load                 s0 <- v0
 * ...
 * </pre>
 *
 * This rewrite will group loads together to see if they can be dup'ed or even removed if a store is
 * directly preceding it.
 */
public class MoveLoadUpPeephole implements BasicBlockPeephole {

  private Value local = null;
  private int stackHeight = 0;
  private Instruction insertPosition = null;

  private final Point firstLoad =
      new Point(
          (i) -> {
            if (i.isLoad() && !i.asLoad().src().hasLocalInfo()) {
              local = i.asLoad().src();
              return true;
            }
            return false;
          });
  private final Wildcard canMoveOver =
      new Wildcard(
          (i) -> {
            if (i.isArgument() || i.isMoveException() || (i.isStore() && i.outValue() == local)) {
              return false;
            }
            stackHeight += PeepholeHelper.numberOfValuesPutOnStack(i);
            if (stackHeight > 0) {
              return false;
            }
            stackHeight -= PeepholeHelper.numberOfValuesConsumedFromStack(i);
            if (stackHeight == 0 && !i.isDebugPosition()) {
              insertPosition = i;
            }
            return true;
          });

  // This searches in reverse, so the pattern is built from the bottom.
  private final PeepholeLayout layout = PeepholeLayout.lookBackward(firstLoad, canMoveOver);

  @Override
  public boolean match(InstructionListIterator it) {
    stackHeight = 0;
    insertPosition = null;
    Match match = layout.test(it);
    if (match == null || insertPosition == null) {
      return false;
    }
    Load oldLoad = firstLoad.get(match).asLoad();
    assert !oldLoad.src().hasLocalInfo();

    StackValue oldStackValue = (StackValue) oldLoad.outValue();
    StackValue newStackValue = oldStackValue.duplicate(oldStackValue.getHeight());
    oldStackValue.replaceUsers(newStackValue);
    oldLoad.src().removeUser(oldLoad);

    it.removeOrReplaceByDebugLocalRead();

    // Find the place to insert a new load.
    Instruction current = it.previous();
    int moves = 1;
    while (current != insertPosition) {
      current = it.previous();
      moves++;
    }

    // Insert directly above the other load.
    Load newLoad = new Load(newStackValue, oldLoad.src());
    newLoad.setPosition(insertPosition.getPosition());
    it.add(newLoad);

    PeepholeHelper.resetPrevious(it, moves);
    return true;
  }
}
