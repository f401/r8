// Copyright (c) 2023, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.examples.ifstatements;

import com.android.tools.r8.TestParameters;
import com.android.tools.r8.TestParametersCollection;
import com.android.tools.r8.examples.ExamplesTestBase;
import com.android.tools.r8.utils.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class IfStatementsTestRunner extends ExamplesTestBase {

  @Parameterized.Parameters(name = "{0}")
  public static TestParametersCollection data() {
    return getTestParameters().withAllRuntimesAndApiLevels().enableApiLevelsForCf().build();
  }

  public IfStatementsTestRunner(TestParameters parameters) {
    super(parameters);
  }

  @Override
  public Class<?> getMainClass() {
    return IfStatements.class;
  }

  @Override
  public String getExpected() {
    return StringUtils.lines(
        "sisnotnull",
        "sisnull",
        "ifCond x != 0",
        "ifCond x < 0",
        "ifCond x <= 0",
        "ifCond x == 0",
        "ifCond x >= 0",
        "ifCond x <= 0",
        "ifCond x != 0",
        "ifCond x >= 0",
        "ifCond x > 0",
        "ifIcmp x != y",
        "ifIcmp x < y",
        "ifIcmp x <= y",
        "ifIcmp x == y",
        "ifIcmp x >= y",
        "ifIcmp x <= y",
        "ifIcmp x != y",
        "ifIcmp x >= y",
        "ifIcmp x > y",
        "ifAcmp a == b",
        "ifAcmp a != b");
  }

  @Test
  public void testDesugaring() throws Exception {
    runTestDesugaring();
  }

  @Test
  public void testR8() throws Exception {
    runTestR8();
  }

  @Test
  public void testDebug() throws Exception {
    runTestDebugComparator();
  }
}
