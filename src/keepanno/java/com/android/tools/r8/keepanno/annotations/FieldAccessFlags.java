// Copyright (c) 2023, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.keepanno.annotations;

/**
 * Valid matches on field access flags and their negations.
 *
 * <p>The negated elements make it easier to express the inverse as we cannot use a "not/negation"
 * operation syntactically.
 */
public enum FieldAccessFlags {
  // General member flags.
  PUBLIC,
  NON_PUBLIC,
  PRIVATE,
  NON_PRIVATE,
  PROTECTED,
  NON_PROTECTED,
  PACKAGE_PRIVATE,
  NON_PACKAGE_PRIVATE,
  STATIC,
  NON_STATIC,
  FINAL,
  NON_FINAL,
  SYNTHETIC,
  NON_SYNTHETIC,
  // Field specific flags.
  VOLATILE,
  NON_VOLATILE,
  TRANSIENT,
  NON_TRANSIENT,
  // ENUM - No PG parser support.
}
