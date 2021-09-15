// Copyright (c) 2021, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.desugar.records;

import static com.android.tools.r8.desugar.records.RecordTestUtils.RECORD_KEEP_RULE;

import com.android.tools.r8.TestBase;
import com.android.tools.r8.TestParameters;
import com.android.tools.r8.TestRuntime.CfRuntime;
import com.android.tools.r8.utils.InternalOptions.TestingOptions;
import com.android.tools.r8.utils.StringUtils;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class RecordInstanceOfTest extends TestBase {

  private static final String RECORD_NAME = "RecordInstanceOf";
  private static final byte[][] PROGRAM_DATA = RecordTestUtils.getProgramData(RECORD_NAME);
  private static final String MAIN_TYPE = RecordTestUtils.getMainType(RECORD_NAME);
  private static final String EXPECTED_RESULT = StringUtils.lines("true", "true", "false");

  private final TestParameters parameters;

  public RecordInstanceOfTest(TestParameters parameters) {
    this.parameters = parameters;
  }

  @Parameterized.Parameters(name = "{0}")
  public static List<Object[]> data() {
    // TODO(b/174431251): This should be replaced with .withCfRuntimes(start = jdk15).
    return buildParameters(
        getTestParameters()
            .withCustomRuntime(CfRuntime.getCheckedInJdk16())
            .withDexRuntimes()
            .withAllApiLevelsAlsoForCf()
            .build());
  }

  @Test
  public void testD8AndJvm() throws Exception {
    if (parameters.isCfRuntime()) {
      testForJvm()
          .addProgramClassFileData(PROGRAM_DATA)
          .enablePreview()
          .run(parameters.getRuntime(), MAIN_TYPE)
          .assertSuccessWithOutput(EXPECTED_RESULT);
    }
    testForD8(parameters.getBackend())
        .addProgramClassFileData(PROGRAM_DATA)
        .setMinApi(parameters.getApiLevel())
        .addOptionsModification(TestingOptions::allowExperimentClassFileVersion)
        .compile()
        .run(parameters.getRuntime(), MAIN_TYPE)
        .assertSuccessWithOutput(EXPECTED_RESULT);
  }

  @Test
  public void testR8() throws Exception {
    if (parameters.isCfRuntime()) {
      testForR8(parameters.getBackend())
          .addProgramClassFileData(PROGRAM_DATA)
          .setMinApi(parameters.getApiLevel())
          .addKeepRules(RECORD_KEEP_RULE)
          .addKeepMainRule(MAIN_TYPE)
          .addLibraryFiles(RecordTestUtils.getJdk15LibraryFiles(temp))
          .addOptionsModification(TestingOptions::allowExperimentClassFileVersion)
          .compile()
          .inspect(RecordTestUtils::assertRecordsAreRecords)
          .enableJVMPreview()
          .run(parameters.getRuntime(), MAIN_TYPE)
          .assertSuccessWithOutput(EXPECTED_RESULT);
      return;
    }
    testForR8(parameters.getBackend())
        .addProgramClassFileData(PROGRAM_DATA)
        .setMinApi(parameters.getApiLevel())
        .addKeepRules(RECORD_KEEP_RULE)
        .addKeepMainRule(MAIN_TYPE)
        .addOptionsModification(TestingOptions::allowExperimentClassFileVersion)
        .compile()
        .run(parameters.getRuntime(), MAIN_TYPE)
        .assertSuccessWithOutput(EXPECTED_RESULT);
  }
}
