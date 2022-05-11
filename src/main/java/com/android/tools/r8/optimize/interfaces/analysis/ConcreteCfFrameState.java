// Copyright (c) 2022, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.optimize.interfaces.analysis;

import static com.android.tools.r8.cf.code.CfFrame.getInitializedFrameType;

import com.android.tools.r8.cf.code.CfAssignability;
import com.android.tools.r8.cf.code.CfFrame;
import com.android.tools.r8.cf.code.CfFrame.FrameType;
import com.android.tools.r8.cf.code.frame.SingleFrameType;
import com.android.tools.r8.cf.code.frame.WideFrameType;
import com.android.tools.r8.graph.AppView;
import com.android.tools.r8.graph.DexMethod;
import com.android.tools.r8.graph.DexType;
import com.android.tools.r8.graph.ProgramMethod;
import com.android.tools.r8.ir.code.ValueType;
import com.google.common.collect.Iterables;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public class ConcreteCfFrameState extends CfFrameState {

  private final Int2ObjectAVLTreeMap<FrameType> locals;
  private final ArrayDeque<FrameType> stack;

  ConcreteCfFrameState() {
    this(new Int2ObjectAVLTreeMap<>(), new ArrayDeque<>());
  }

  ConcreteCfFrameState(Int2ObjectAVLTreeMap<FrameType> locals, ArrayDeque<FrameType> stack) {
    this.locals = locals;
    this.stack = stack;
  }

  @Override
  public CfFrameState clone() {
    return new ConcreteCfFrameState(locals.clone(), stack.clone());
  }

  @Override
  public boolean isConcrete() {
    return true;
  }

  @Override
  public ConcreteCfFrameState asConcrete() {
    return this;
  }

  @Override
  public CfFrameState check(AppView<?> appView, CfFrame frame) {
    CfFrame currentFrame = CfFrame.builder().setLocals(locals).setStack(stack).build();
    if (CfAssignability.isFrameAssignable(currentFrame, frame, appView).isFailed()) {
      return error();
    }
    CfFrame frameCopy = frame.mutableCopy();
    return new ConcreteCfFrameState(frameCopy.getMutableLocals(), frameCopy.getMutableStack());
  }

  @Override
  public CfFrameState clear() {
    return bottom();
  }

  @Override
  public CfFrameState markInitialized(FrameType uninitializedType, DexType initializedType) {
    if (uninitializedType.isInitialized()) {
      return error();
    }
    for (Int2ObjectMap.Entry<FrameType> entry : locals.int2ObjectEntrySet()) {
      FrameType frameType = entry.getValue();
      FrameType initializedFrameType =
          getInitializedFrameType(uninitializedType, frameType, initializedType);
      entry.setValue(initializedFrameType);
    }
    // TODO(b/214496607): By using a collection that supports element replacement this could mutate
    //  the existing stack instead of building a new one.
    ArrayDeque<FrameType> newStack = new ArrayDeque<>();
    for (FrameType frameType : stack) {
      FrameType initializedFrameType =
          getInitializedFrameType(uninitializedType, frameType, initializedType);
      newStack.addLast(initializedFrameType);
    }
    return new ConcreteCfFrameState(locals, newStack);
  }

  @Override
  public CfFrameState pop() {
    if (stack.isEmpty()) {
      return error();
    }
    stack.removeLast();
    return this;
  }

  @Override
  public CfFrameState pop(BiFunction<CfFrameState, FrameType, CfFrameState> fn) {
    if (stack.isEmpty()) {
      return error();
    }
    FrameType frameType = stack.removeLast();
    return fn.apply(this, frameType);
  }

  @Override
  public CfFrameState popAndInitialize(
      AppView<?> appView, DexMethod constructor, ProgramMethod context) {
    return pop(
        (state, frameType) -> {
          if (frameType.isUninitializedThis()) {
            if (constructor.getHolderType() == context.getHolderType()
                || constructor.getHolderType() == context.getHolder().getSuperType()) {
              return state.markInitialized(frameType, context.getHolderType());
            }
          } else if (frameType.isUninitializedNew()) {
            DexType uninitializedNewType = frameType.getUninitializedNewType();
            if (constructor.getHolderType() == uninitializedNewType) {
              return state.markInitialized(frameType, uninitializedNewType);
            }
          }
          return error();
        });
  }

  @Override
  public CfFrameState popInitialized(
      AppView<?> appView,
      DexType expectedType,
      BiFunction<CfFrameState, FrameType, CfFrameState> fn) {
    return pop(
        (state, frameType) ->
            frameType.isInitialized()
                    && CfAssignability.isAssignable(
                        frameType.getInitializedType(appView.dexItemFactory()),
                        expectedType,
                        appView)
                ? fn.apply(state, frameType)
                : error());
  }

  @Override
  public CfFrameState popInitialized(AppView<?> appView, DexType... expectedTypes) {
    CfFrameState state = this;
    for (int i = expectedTypes.length - 1; i >= 0; i--) {
      state = state.popInitialized(appView, expectedTypes[i]);
    }
    return state;
  }

  @Override
  public CfFrameState push(DexType type) {
    return push(FrameType.initialized(type));
  }

  @Override
  public CfFrameState push(FrameType frameType) {
    stack.addLast(frameType);
    return this;
  }

  @Override
  public CfFrameState readLocal(
      AppView<?> appView,
      int localIndex,
      ValueType expectedType,
      BiFunction<CfFrameState, FrameType, CfFrameState> fn) {
    FrameType frameType = locals.get(localIndex);
    if (frameType == null) {
      return error();
    }
    if (frameType.isInitialized()
        && CfAssignability.isAssignable(
            frameType.getInitializedType(appView.dexItemFactory()), expectedType, appView)) {
      return fn.apply(this, frameType);
    }
    if (frameType.isUninitializedObject() && expectedType.isObject()) {
      return fn.apply(this, frameType);
    }
    return error();
  }

  @Override
  public CfFrameState storeLocal(int localIndex, FrameType frameType) {
    locals.put(localIndex, frameType);
    if (frameType.isWide()) {
      locals.put(localIndex + 1, frameType);
    }
    return this;
  }

  public CfFrameState join(
      ConcreteCfFrameState state, UnaryOperator<FrameType> joinWithMissingLocal) {
    CfFrame.Builder builder = CfFrame.builder();
    joinLocals(state.locals, builder, joinWithMissingLocal);
    ErroneousCfFrameState error = joinStack(state.stack, builder);
    if (error != null) {
      return error;
    }
    CfFrame frame = builder.buildMutable();
    return new ConcreteCfFrameState(frame.getMutableLocals(), frame.getMutableStack());
  }

  private void joinLocals(
      Int2ObjectSortedMap<FrameType> locals,
      CfFrame.Builder builder,
      UnaryOperator<FrameType> joinWithMissingLocal) {
    ObjectBidirectionalIterator<Entry<FrameType>> iterator =
        this.locals.int2ObjectEntrySet().iterator();
    ObjectBidirectionalIterator<Entry<FrameType>> otherIterator =
        locals.int2ObjectEntrySet().iterator();
    while (iterator.hasNext() && otherIterator.hasNext()) {
      Entry<FrameType> entry = nextLocal(iterator);
      int localIndex = entry.getIntKey();
      FrameType frameType = entry.getValue();

      Entry<FrameType> otherEntry = nextLocal(otherIterator);
      int otherLocalIndex = otherEntry.getIntKey();
      FrameType otherFrameType = otherEntry.getValue();

      if (localIndex < otherLocalIndex) {
        joinLocalsWithDifferentIndices(
            localIndex,
            frameType,
            otherLocalIndex,
            otherFrameType,
            iterator,
            otherIterator,
            builder);
      } else if (otherLocalIndex < localIndex) {
        joinLocalsWithDifferentIndices(
            otherLocalIndex,
            otherFrameType,
            localIndex,
            frameType,
            otherIterator,
            iterator,
            builder);
      } else {
        joinLocalsWithSameIndex(
            localIndex, frameType, otherFrameType, iterator, otherIterator, builder);
      }
    }
    joinLocalsOnlyPresentInOne(iterator, builder, joinWithMissingLocal);
    joinLocalsOnlyPresentInOne(otherIterator, builder, joinWithMissingLocal);
  }

  private void joinLocalsWithDifferentIndices(
      int localIndex,
      FrameType frameType,
      int otherLocalIndex,
      FrameType otherFrameType,
      ObjectBidirectionalIterator<Entry<FrameType>> iterator,
      ObjectBidirectionalIterator<Entry<FrameType>> otherIterator,
      CfFrame.Builder builder) {
    assert localIndex < otherLocalIndex;

    // Check if the smaller local does not overlap with the larger local.
    if (frameType.isSingle() || localIndex + 1 < otherLocalIndex) {
      setLocalToTop(localIndex, frameType, builder);
      previousLocal(otherIterator);
      return;
    }

    // The smaller local is a wide that overlaps with the larger local.
    setLocalToTop(localIndex, frameType, builder);

    // If the larger local is a wide, then its high part is no longer usable. We also need to handle
    // overlapping of the other local and the next local in the iterator.
    if (otherFrameType.isWide()) {
      int lastLocalIndexMarkedTop = otherLocalIndex + 1;
      setSingleLocalToTop(lastLocalIndexMarkedTop, builder);
      handleOverlappingLocals(lastLocalIndexMarkedTop, iterator, otherIterator, builder);
    }
  }

  private void joinLocalsWithSameIndex(
      int localIndex,
      FrameType frameType,
      FrameType otherFrameType,
      ObjectBidirectionalIterator<Entry<FrameType>> iterator,
      ObjectBidirectionalIterator<Entry<FrameType>> otherIterator,
      CfFrame.Builder builder) {
    if (frameType.isSingle()) {
      if (otherFrameType.isSingle()) {
        joinSingleLocalsWithSameIndex(
            localIndex, frameType.asSingle(), otherFrameType.asSingle(), builder);
      } else {
        setWideLocalToTop(localIndex, builder);
        handleOverlappingLocals(localIndex + 1, iterator, otherIterator, builder);
      }
    } else {
      if (otherFrameType.isWide()) {
        joinWideLocalsWithSameIndex(
            localIndex, frameType.asWide(), otherFrameType.asWide(), builder);
      } else {
        setWideLocalToTop(localIndex, builder);
        handleOverlappingLocals(localIndex + 1, otherIterator, iterator, builder);
      }
    }
  }

  private void joinSingleLocalsWithSameIndex(
      int localIndex,
      SingleFrameType frameType,
      SingleFrameType otherFrameType,
      CfFrame.Builder builder) {
    builder.store(localIndex, frameType.join(otherFrameType).asFrameType());
  }

  private void joinWideLocalsWithSameIndex(
      int localIndex,
      WideFrameType frameType,
      WideFrameType otherFrameType,
      CfFrame.Builder builder) {
    builder.store(localIndex, frameType.join(otherFrameType).asFrameType());
  }

  // TODO(b/231521474): By splitting each wide type into single left/right types, the join of each
  //  (single) local index can be determined by looking at only locals[i] and otherLocals[i] (i.e.,
  //  there is no carry-over). Thus this entire method could be avoided.
  private void handleOverlappingLocals(
      int lastLocalIndexMarkedTop,
      ObjectBidirectionalIterator<Entry<FrameType>> iterator,
      ObjectBidirectionalIterator<Entry<FrameType>> otherIterator,
      CfFrame.Builder builder) {
    ObjectBidirectionalIterator<Entry<FrameType>> currentIterator = iterator;
    while (currentIterator.hasNext()) {
      Entry<FrameType> entry = nextLocal(currentIterator);
      int currentLocalIndex = entry.getIntKey();
      FrameType currentFrameType = entry.getValue();

      // Check if this local overlaps with the previous wide local that was set to top. If not, then
      // this local is not affected.
      if (lastLocalIndexMarkedTop < currentLocalIndex) {
        // The current local still needs to be handled, thus this rewinds the iterator.
        previousLocal(currentIterator);
        break;
      }

      // Verify that the low part of the current local has been set to top.
      assert builder.hasLocal(currentLocalIndex);
      assert builder.getLocal(currentLocalIndex).isOneWord();

      // If the current local is not a wide, then we're done.
      if (currentFrameType.isSingle()) {
        // The current local has become top due to the overlap with a wide local. Therefore, this
        // intentionally does not rewind the iterator.
        break;
      }

      // The current local is a wide. We mark its high local index as top due to the overlap, and
      // check if this wide local overlaps with a wide local in the other locals.
      lastLocalIndexMarkedTop = currentLocalIndex + 1;
      setSingleLocalToTop(lastLocalIndexMarkedTop, builder);
      currentIterator = currentIterator == iterator ? otherIterator : iterator;
    }
  }

  private void joinLocalsOnlyPresentInOne(
      ObjectBidirectionalIterator<Entry<FrameType>> iterator,
      CfFrame.Builder builder,
      UnaryOperator<FrameType> joinWithMissingLocal) {
    while (iterator.hasNext()) {
      Entry<FrameType> entry = nextLocal(iterator);
      int localIndex = entry.getIntKey();
      FrameType frameType = entry.getValue();
      FrameType joinFrameType = joinWithMissingLocal.apply(frameType);
      assert joinFrameType.isSingle() == frameType.isSingle();
      if (joinFrameType.isOneWord() || joinFrameType.isTwoWord()) {
        setLocalToTop(localIndex, joinFrameType, builder);
      } else {
        builder.store(localIndex, joinFrameType);
      }
    }
  }

  private Entry<FrameType> nextLocal(ObjectBidirectionalIterator<Entry<FrameType>> iterator) {
    Entry<FrameType> entry = iterator.next();
    FrameType frameType = entry.getValue();
    if (frameType.isWide()) {
      assert frameType.isDouble() || frameType.isLong();
      Entry<FrameType> highEntry = iterator.next();
      assert highEntry.getIntKey() == entry.getIntKey() + 1;
      assert highEntry.getValue() == frameType;
    }
    return entry;
  }

  private void previousLocal(ObjectBidirectionalIterator<Entry<FrameType>> iterator) {
    Entry<FrameType> entry = iterator.previous();
    FrameType frameType = entry.getValue();
    if (frameType.isWide()) {
      assert frameType.isDouble() || frameType.isLong();
      Entry<FrameType> lowEntry = iterator.previous();
      assert lowEntry.getIntKey() == entry.getIntKey() - 1;
      assert lowEntry.getValue() == frameType;
    }
  }

  private void setLocalToTop(int localIndex, FrameType frameType, CfFrame.Builder builder) {
    if (frameType.isSingle()) {
      setSingleLocalToTop(localIndex, builder);
    } else {
      setWideLocalToTop(localIndex, builder);
    }
  }

  private void setSingleLocalToTop(int localIndex, CfFrame.Builder builder) {
    assert !builder.hasLocal(localIndex);
    builder.store(localIndex, FrameType.oneWord());
  }

  private void setWideLocalToTop(int localIndex, CfFrame.Builder builder) {
    assert !builder.hasLocal(localIndex);
    assert !builder.hasLocal(localIndex + 1);
    setSingleLocalToTop(localIndex, builder);
    setSingleLocalToTop(localIndex + 1, builder);
  }

  private ErroneousCfFrameState joinStack(Deque<FrameType> stack, CfFrame.Builder builder) {
    Iterator<FrameType> iterator = this.stack.iterator();
    Iterator<FrameType> otherIterator = stack.iterator();
    while (iterator.hasNext() && otherIterator.hasNext()) {
      FrameType frameType = iterator.next();
      FrameType otherFrameType = otherIterator.next();
      if (frameType.isSingle() != otherFrameType.isSingle()) {
        return error();
      }
      if (frameType.isSingle()) {
        SingleFrameType join = frameType.asSingle().join(otherFrameType.asSingle());
        if (join.isOneWord()) {
          return error();
        }
        builder.push(join.asFrameType());
      } else {
        WideFrameType join = frameType.asWide().join(otherFrameType.asWide());
        if (join.isTwoWord()) {
          return error();
        }
        builder.push(join.asFrameType());
      }
    }
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConcreteCfFrameState that = (ConcreteCfFrameState) o;
    return locals.equals(that.locals) && Iterables.elementsEqual(stack, that.stack);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locals, stack);
  }
}