// Copyright (c) 2021, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.kotlin.metadata.inline_class_fun_descriptor_classes_lib

@JvmInline
value class Password(val s: String)

@KeepForApi
fun create(pw : String) : Password {
  return Password(pw)
}

@KeepForApi
fun login(pw : Password) {
  println(pw.s)
}
