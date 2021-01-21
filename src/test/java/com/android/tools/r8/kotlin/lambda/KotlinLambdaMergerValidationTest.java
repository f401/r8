// Copyright (c) 2019, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.kotlin.lambda;

import static com.android.tools.r8.ToolHelper.getKotlinCompilers;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assume.assumeTrue;

import com.android.tools.r8.KotlinCompilerTool.KotlinCompiler;
import com.android.tools.r8.TestParameters;
import com.android.tools.r8.TestRuntime;
import com.android.tools.r8.TestRuntime.CfRuntime;
import com.android.tools.r8.ToolHelper;
import com.android.tools.r8.ToolHelper.KotlinTargetVersion;
import com.android.tools.r8.kotlin.AbstractR8KotlinTestBase;
import com.android.tools.r8.utils.DescriptorUtils;
import java.nio.file.Path;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class KotlinLambdaMergerValidationTest extends AbstractR8KotlinTestBase {

  private final TestParameters parameters;

  @Parameterized.Parameters(name = "{0}, target: {1}, kotlinc: {2}")
  public static Collection<Object[]> data() {
    return buildParameters(
        getTestParameters().withAllRuntimesAndApiLevels().build(),
        KotlinTargetVersion.values(),
        getKotlinCompilers());
  }

  public KotlinLambdaMergerValidationTest(
      TestParameters parameters, KotlinTargetVersion targetVersion, KotlinCompiler kotlinc) {
    super(targetVersion, kotlinc, false);
    this.parameters = parameters;
  }

  @Test
  public void testR8_excludeKotlinStdlib() throws Exception {
    assumeTrue(parameters.isCfRuntime());

    String pkg = getClass().getPackage().getName();
    String folder = DescriptorUtils.getBinaryNameFromJavaType(pkg);
    CfRuntime cfRuntime =
        parameters.isCfRuntime() ? parameters.getRuntime().asCf() : TestRuntime.getCheckedInJdk9();
    Path ktClasses =
        kotlinc(cfRuntime, kotlinc, targetVersion)
            .addSourceFiles(getKotlinFileInTest(folder, "b143165163"))
            .compile();
    testForR8(parameters.getBackend())
        .addLibraryFiles(ToolHelper.getJava8RuntimeJar())
        .addLibraryFiles(ToolHelper.getKotlinStdlibJar(kotlinc))
        .addProgramFiles(ktClasses)
        .addKeepMainRule("**.B143165163Kt")
        .addDontWarnJetBrainsAnnotations()
        .setMinApi(parameters.getApiLevel())
        .compile()
        .addRunClasspathFiles(ToolHelper.getKotlinStdlibJar(kotlinc))
        .run(parameters.getRuntime(), pkg + ".B143165163Kt")
        .assertSuccessWithOutputLines("outer foo bar", "outer foo default");
  }

  @Test
  public void testR8_includeKotlinStdlib() throws Exception {
    assumeTrue(parameters.isDexRuntime());

    String pkg = getClass().getPackage().getName();
    String folder = DescriptorUtils.getBinaryNameFromJavaType(pkg);
    CfRuntime cfRuntime =
        parameters.isCfRuntime() ? parameters.getRuntime().asCf() : TestRuntime.getCheckedInJdk9();
    Path ktClasses =
        kotlinc(cfRuntime, kotlinc, targetVersion)
            .addSourceFiles(getKotlinFileInTest(folder, "b143165163"))
            .compile();
    testForR8(parameters.getBackend())
        .addProgramFiles(ToolHelper.getKotlinStdlibJar(kotlinc))
        .addProgramFiles(ktClasses)
        .addKeepMainRule("**.B143165163Kt")
        .addDontWarnJetBrainsAnnotations()
        .allowDiagnosticWarningMessages()
        .setMinApi(parameters.getApiLevel())
        .compile()
        .assertAllWarningMessagesMatch(equalTo("Resource 'META-INF/MANIFEST.MF' already exists."))
        .run(parameters.getRuntime(), pkg + ".B143165163Kt")
        .assertSuccessWithOutputLines("outer foo bar", "outer foo default");
  }
}
