// Copyright (c) 2021, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.optimize.argumentpropagation.codescanner;

public class UnknownParameterState extends ParameterState {

  private static final UnknownParameterState INSTANCE = new UnknownParameterState();

  private UnknownParameterState() {}

  public static UnknownParameterState get() {
    return INSTANCE;
  }

  @Override
  public boolean isUnknown() {
    return true;
  }
}
