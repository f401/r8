// Copyright (c) 2020, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.graph;

import com.android.tools.r8.utils.InternalOptions;
import com.android.tools.r8.utils.PredicateUtils;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MethodArrayBacking {

  private DexEncodedMethod[] directMethods = DexEncodedMethod.EMPTY_ARRAY;
  private DexEncodedMethod[] virtualMethods = DexEncodedMethod.EMPTY_ARRAY;

  int size() {
    return directMethods.length + virtualMethods.length;
  }

  void forEachMethod(Consumer<DexEncodedMethod> consumer) {
    for (DexEncodedMethod method : directMethods()) {
      consumer.accept(method);
    }
    for (DexEncodedMethod method : virtualMethods()) {
      consumer.accept(method);
    }
  }

  List<DexEncodedMethod> directMethods() {
    assert directMethods != null;
    if (InternalOptions.assertionsEnabled()) {
      return Collections.unmodifiableList(Arrays.asList(directMethods));
    }
    return Arrays.asList(directMethods);
  }

  void appendDirectMethod(DexEncodedMethod method) {
    DexEncodedMethod[] newMethods = new DexEncodedMethod[directMethods.length + 1];
    System.arraycopy(directMethods, 0, newMethods, 0, directMethods.length);
    newMethods[directMethods.length] = method;
    directMethods = newMethods;
    assert verifyNoDuplicateMethods();
  }

  void appendDirectMethods(Collection<DexEncodedMethod> methods) {
    DexEncodedMethod[] newMethods = new DexEncodedMethod[directMethods.length + methods.size()];
    System.arraycopy(directMethods, 0, newMethods, 0, directMethods.length);
    int i = directMethods.length;
    for (DexEncodedMethod method : methods) {
      newMethods[i] = method;
      i++;
    }
    directMethods = newMethods;
    assert verifyNoDuplicateMethods();
  }

  void removeDirectMethod(int index) {
    DexEncodedMethod[] newMethods = new DexEncodedMethod[directMethods.length - 1];
    System.arraycopy(directMethods, 0, newMethods, 0, index);
    System.arraycopy(directMethods, index + 1, newMethods, index, directMethods.length - index - 1);
    directMethods = newMethods;
  }

  void removeDirectMethod(DexMethod method) {
    int index = -1;
    for (int i = 0; i < directMethods.length; i++) {
      if (method.match(directMethods[i])) {
        index = i;
        break;
      }
    }
    if (index >= 0) {
      removeDirectMethod(index);
    }
  }

  void setDirectMethod(int index, DexEncodedMethod method) {
    directMethods[index] = method;
    assert verifyNoDuplicateMethods();
  }

  void setDirectMethods(DexEncodedMethod[] methods) {
    directMethods = MoreObjects.firstNonNull(methods, DexEncodedMethod.EMPTY_ARRAY);
    assert verifyNoDuplicateMethods();
  }

  List<DexEncodedMethod> virtualMethods() {
    assert virtualMethods != null;
    if (InternalOptions.assertionsEnabled()) {
      return Collections.unmodifiableList(Arrays.asList(virtualMethods));
    }
    return Arrays.asList(virtualMethods);
  }

  void appendVirtualMethod(DexEncodedMethod method) {
    DexEncodedMethod[] newMethods = new DexEncodedMethod[virtualMethods.length + 1];
    System.arraycopy(virtualMethods, 0, newMethods, 0, virtualMethods.length);
    newMethods[virtualMethods.length] = method;
    virtualMethods = newMethods;
    assert verifyNoDuplicateMethods();
  }

  void appendVirtualMethods(Collection<DexEncodedMethod> methods) {
    DexEncodedMethod[] newMethods = new DexEncodedMethod[virtualMethods.length + methods.size()];
    System.arraycopy(virtualMethods, 0, newMethods, 0, virtualMethods.length);
    int i = virtualMethods.length;
    for (DexEncodedMethod method : methods) {
      newMethods[i] = method;
      i++;
    }
    virtualMethods = newMethods;
    assert verifyNoDuplicateMethods();
  }

  void setVirtualMethod(int index, DexEncodedMethod method) {
    virtualMethods[index] = method;
    assert verifyNoDuplicateMethods();
  }

  void setVirtualMethods(DexEncodedMethod[] methods) {
    virtualMethods = MoreObjects.firstNonNull(methods, DexEncodedMethod.EMPTY_ARRAY);
    assert verifyNoDuplicateMethods();
  }

  void virtualizeMethods(Set<DexEncodedMethod> privateInstanceMethods) {
    int vLen = virtualMethods.length;
    int dLen = directMethods.length;
    int mLen = privateInstanceMethods.size();
    assert mLen <= dLen;

    DexEncodedMethod[] newDirectMethods = new DexEncodedMethod[dLen - mLen];
    int index = 0;
    for (int i = 0; i < dLen; i++) {
      DexEncodedMethod encodedMethod = directMethods[i];
      if (!privateInstanceMethods.contains(encodedMethod)) {
        newDirectMethods[index++] = encodedMethod;
      }
    }
    assert index == dLen - mLen;
    setDirectMethods(newDirectMethods);

    DexEncodedMethod[] newVirtualMethods = new DexEncodedMethod[vLen + mLen];
    System.arraycopy(virtualMethods, 0, newVirtualMethods, 0, vLen);
    index = vLen;
    for (DexEncodedMethod encodedMethod : privateInstanceMethods) {
      newVirtualMethods[index++] = encodedMethod;
    }
    setVirtualMethods(newVirtualMethods);
  }

  DexEncodedMethod getDirectMethod(DexMethod method) {
    for (DexEncodedMethod directMethod : directMethods) {
      if (method.match(directMethod)) {
        return directMethod;
      }
    }
    return null;
  }

  DexEncodedMethod getDirectMethod(Predicate<DexEncodedMethod> predicate) {
    return PredicateUtils.findFirst(directMethods, predicate);
  }

  DexEncodedMethod getVirtualMethod(DexMethod method) {
    for (DexEncodedMethod virtualMethod : virtualMethods) {
      if (method.match(virtualMethod)) {
        return virtualMethod;
      }
    }
    return null;
  }

  DexEncodedMethod getVirtualMethod(Predicate<DexEncodedMethod> predicate) {
    return PredicateUtils.findFirst(virtualMethods, predicate);
  }

  DexEncodedMethod getMethod(DexMethod method) {
    DexEncodedMethod result = getDirectMethod(method);
    return result == null ? getVirtualMethod(method) : result;
  }

  void addMethod(DexEncodedMethod method) {
    if (method.accessFlags.isStatic()
        || method.accessFlags.isPrivate()
        || method.accessFlags.isConstructor()) {
      addDirectMethod(method);
    } else {
      addVirtualMethod(method);
    }
  }

  void addVirtualMethod(DexEncodedMethod virtualMethod) {
    assert !virtualMethod.accessFlags.isStatic();
    assert !virtualMethod.accessFlags.isPrivate();
    assert !virtualMethod.accessFlags.isConstructor();
    virtualMethods = Arrays.copyOf(virtualMethods, virtualMethods.length + 1);
    virtualMethods[virtualMethods.length - 1] = virtualMethod;
  }

  void addDirectMethod(DexEncodedMethod staticMethod) {
    assert staticMethod.accessFlags.isStatic()
        || staticMethod.accessFlags.isPrivate()
        || staticMethod.accessFlags.isConstructor();
    directMethods = Arrays.copyOf(directMethods, directMethods.length + 1);
    directMethods[directMethods.length - 1] = staticMethod;
  }

  boolean verifyNoDuplicateMethods() {
    Set<DexMethod> unique = Sets.newIdentityHashSet();
    forEachMethod(
        method -> {
          boolean changed = unique.add(method.method);
          assert changed : "Duplicate method `" + method.method.toSourceString() + "`";
        });
    return true;
  }

  boolean hasAnnotations() {
    for (DexEncodedMethod method : virtualMethods) {
      if (method.hasAnnotation()) {
        return true;
      }
    }
    for (DexEncodedMethod method : directMethods) {
      if (method.hasAnnotation()) {
        return true;
      }
    }
    return false;
  }

  synchronized boolean isSorted() {
    return isSorted(virtualMethods) && isSorted(directMethods);
  }

  private static boolean isSorted(DexEncodedMethod[] items) {
    DexEncodedMethod[] sorted = items.clone();
    Arrays.sort(sorted, Comparator.comparing(DexEncodedMember::toReference));
    return Arrays.equals(items, sorted);
  }

  synchronized void sort() {
    synchronized (virtualMethods) {
      Arrays.sort(virtualMethods, Comparator.comparing(a -> a.method));
    }
    synchronized (directMethods) {
      Arrays.sort(directMethods, Comparator.comparing(a -> a.method));
    }
  }
}
