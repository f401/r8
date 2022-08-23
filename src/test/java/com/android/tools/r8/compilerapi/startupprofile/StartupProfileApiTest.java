// Copyright (c) 2022, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.compilerapi.startupprofile;

import static com.android.tools.r8.utils.codeinspector.Matchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;

import com.android.tools.r8.D8;
import com.android.tools.r8.D8Command;
import com.android.tools.r8.DexIndexedConsumer;
import com.android.tools.r8.ProgramConsumer;
import com.android.tools.r8.R8;
import com.android.tools.r8.R8Command;
import com.android.tools.r8.TestParameters;
import com.android.tools.r8.compilerapi.CompilerApiTest;
import com.android.tools.r8.compilerapi.CompilerApiTestRunner;
import com.android.tools.r8.origin.Origin;
import com.android.tools.r8.references.Reference;
import com.android.tools.r8.startup.StartupProfileBuilder;
import com.android.tools.r8.startup.StartupProfileProvider;
import com.android.tools.r8.utils.ThrowingConsumer;
import com.android.tools.r8.utils.codeinspector.CodeInspector;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.function.BiConsumer;
import org.junit.Test;

public class StartupProfileApiTest extends CompilerApiTestRunner {

  private static final int FIRST_API_LEVEL_WITH_NATIVE_MULTIDEX = 21;

  public StartupProfileApiTest(TestParameters parameters) {
    super(parameters);
  }

  @Override
  public Class<? extends CompilerApiTest> binaryTestClass() {
    return ApiTest.class;
  }

  @Test
  public void testD8ArrayApi() throws Exception {
    ApiTest test = new ApiTest(ApiTest.PARAMETERS);
    runTest(
        test,
        programConsumer ->
            test.runD8(ApiTest::addStartupProfileProviderUsingArrayApi, programConsumer));
  }

  @Test
  public void testD8CollectionApi() throws Exception {
    ApiTest test = new ApiTest(ApiTest.PARAMETERS);
    runTest(
        test,
        programConsumer ->
            test.runD8(ApiTest::addStartupProfileProviderUsingCollectionApi, programConsumer));
  }

  @Test
  public void testR8ArrayApi() throws Exception {
    ApiTest test = new ApiTest(ApiTest.PARAMETERS);
    runTest(
        test,
        programConsumer ->
            test.runR8(ApiTest::addStartupProfileProviderUsingArrayApi, programConsumer));
  }

  @Test
  public void testR8CollectionApi() throws Exception {
    ApiTest test = new ApiTest(ApiTest.PARAMETERS);
    runTest(
        test,
        programConsumer ->
            test.runR8(ApiTest::addStartupProfileProviderUsingCollectionApi, programConsumer));
  }

  private void runTest(ApiTest test, ThrowingConsumer<ProgramConsumer, Exception> testRunner)
      throws Exception {
    Path output = temp.newFolder().toPath();
    testRunner.accept(new DexIndexedConsumer.DirectoryConsumer(output));
    assertThat(
        new CodeInspector(output.resolve("classes.dex")).clazz(test.getMockClass()), isPresent());
    // TODO(b/238173796): The PostStartupMockClass should be in classes2.dex.
    assertThat(
        new CodeInspector(output.resolve("classes.dex")).clazz(test.getPostStartupMockClass()),
        isPresent());
  }

  public static class ApiTest extends CompilerApiTest {

    public ApiTest(Object parameters) {
      super(parameters);
    }

    private StartupProfileProvider getStartupProfileProvider() {
      return new StartupProfileProvider() {
        @Override
        public String get() {
          // Intentionally empty. All uses of this API should be rewritten to use getStartupProfile.
          return "";
        }

        @Override
        public void getStartupProfile(StartupProfileBuilder startupProfileBuilder) {
          startupProfileBuilder.addStartupClass(
              startupClassBuilder ->
                  startupClassBuilder.setClassReference(Reference.classFromClass(getMockClass())));
        }

        @Override
        public Origin getOrigin() {
          return Origin.unknown();
        }
      };
    }

    public void runD8(
        BiConsumer<D8Command.Builder, StartupProfileProvider> startupProfileProviderInstaller,
        ProgramConsumer programConsumer)
        throws Exception {
      D8Command.Builder commandBuilder =
          D8Command.builder()
              .addClassProgramData(getBytesForClass(getMockClass()), Origin.unknown())
              .addClassProgramData(getBytesForClass(getPostStartupMockClass()), Origin.unknown())
              .addLibraryFiles(getJava8RuntimeJar())
              .setMinApiLevel(FIRST_API_LEVEL_WITH_NATIVE_MULTIDEX)
              .setProgramConsumer(programConsumer);
      startupProfileProviderInstaller.accept(commandBuilder, getStartupProfileProvider());
      D8.run(commandBuilder.build());
    }

    public void runR8(
        BiConsumer<R8Command.Builder, StartupProfileProvider> startupProfileProviderInstaller,
        ProgramConsumer programConsumer)
        throws Exception {
      R8Command.Builder commandBuilder =
          R8Command.builder()
              .addClassProgramData(getBytesForClass(getMockClass()), Origin.unknown())
              .addClassProgramData(getBytesForClass(getPostStartupMockClass()), Origin.unknown())
              .addProguardConfiguration(
                  Collections.singletonList("-keep class * { *; }"), Origin.unknown())
              .addLibraryFiles(getJava8RuntimeJar())
              .setMinApiLevel(FIRST_API_LEVEL_WITH_NATIVE_MULTIDEX)
              .setProgramConsumer(programConsumer);
      startupProfileProviderInstaller.accept(commandBuilder, getStartupProfileProvider());
      R8.run(commandBuilder.build());
    }

    @Test
    public void testD8ArrayApi() throws Exception {
      runD8(ApiTest::addStartupProfileProviderUsingArrayApi, DexIndexedConsumer.emptyConsumer());
    }

    private static void addStartupProfileProviderUsingArrayApi(
        D8Command.Builder commandBuilder, StartupProfileProvider startupProfileProvider) {
      StartupProfileProvider[] startupProfileProviders =
          new StartupProfileProvider[] {startupProfileProvider};
      commandBuilder.addStartupProfileProviders(startupProfileProviders);
    }

    @Test
    public void testD8CollectionApi() throws Exception {
      runD8(
          ApiTest::addStartupProfileProviderUsingCollectionApi, DexIndexedConsumer.emptyConsumer());
    }

    private static void addStartupProfileProviderUsingCollectionApi(
        D8Command.Builder commandBuilder, StartupProfileProvider startupProfileProvider) {
      Collection<StartupProfileProvider> startupProfileProviders =
          Collections.singleton(startupProfileProvider);
      commandBuilder.addStartupProfileProviders(startupProfileProviders);
    }

    @Test
    public void testR8ArrayApi() throws Exception {
      runR8(ApiTest::addStartupProfileProviderUsingArrayApi, DexIndexedConsumer.emptyConsumer());
    }

    private static void addStartupProfileProviderUsingArrayApi(
        R8Command.Builder commandBuilder, StartupProfileProvider startupProfileProvider) {
      StartupProfileProvider[] startupProfileProviders =
          new StartupProfileProvider[] {startupProfileProvider};
      commandBuilder.addStartupProfileProviders(startupProfileProviders);
    }

    @Test
    public void testR8CollectionApi() throws Exception {
      runR8(
          ApiTest::addStartupProfileProviderUsingCollectionApi, DexIndexedConsumer.emptyConsumer());
    }

    private static void addStartupProfileProviderUsingCollectionApi(
        R8Command.Builder commandBuilder, StartupProfileProvider startupProfileProvider) {
      Collection<StartupProfileProvider> startupProfileProviders =
          Collections.singleton(startupProfileProvider);
      commandBuilder.addStartupProfileProviders(startupProfileProviders);
    }
  }
}
