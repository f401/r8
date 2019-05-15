// Copyright (c) 2019, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.shaking.librarymethodoverride;

import static com.android.tools.r8.utils.codeinspector.Matchers.isPresent;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.android.tools.r8.AssumeMayHaveSideEffects;
import com.android.tools.r8.NeverClassInline;
import com.android.tools.r8.NeverMerge;
import com.android.tools.r8.TestBase;
import com.android.tools.r8.TestParameters;
import com.android.tools.r8.TestParametersCollection;
import com.android.tools.r8.utils.codeinspector.ClassSubject;
import com.android.tools.r8.utils.codeinspector.CodeInspector;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class LibraryMethodOverrideTest extends TestBase {

  private final TestParameters parameters;

  @Parameterized.Parameters(name = "{0}")
  public static TestParametersCollection data() {
    return getTestParameters().withAllRuntimes().build();
  }

  public LibraryMethodOverrideTest(TestParameters parameters) {
    this.parameters = parameters;
  }

  @Test
  public void test() throws Exception {
    testForR8(parameters.getBackend())
        .addInnerClasses(LibraryMethodOverrideTest.class)
        .addKeepMainRule(TestClass.class)
        .addOptionsModification(options -> options.enableTreeShakingOfLibraryMethodOverrides = true)
        .enableClassInliningAnnotations()
        .enableMergeAnnotations()
        .enableSideEffectAnnotations()
        .setMinApi(parameters.getRuntime())
        .compile()
        .inspect(this::verifyOutput)
        .run(parameters.getRuntime(), TestClass.class)
        .assertSuccessWithOutputLines(
            "EscapesDirectly",
            "EscapesIndirectly",
            "EscapesIndirectlyWithOverrideSub",
            "DoesNotEscapeWithSubThatOverridesAndEscapesSub");
  }

  private void verifyOutput(CodeInspector inspector) {
    List<Class<?>> nonEscapingClasses =
        ImmutableList.of(
            DoesNotEscape.class,
            DoesNotEscapeWithSubThatDoesNotOverride.class,
            DoesNotEscapeWithSubThatDoesNotOverrideSub.class,
            DoesNotEscapeWithSubThatOverrides.class,
            DoesNotEscapeWithSubThatOverridesSub.class,
            DoesNotEscapeWithSubThatOverridesAndEscapes.class);
    for (Class<?> nonEscapingClass : nonEscapingClasses) {
      ClassSubject classSubject = inspector.clazz(nonEscapingClass);
      assertThat(classSubject, isPresent());
      assertThat(classSubject.uniqueMethodWithName("toString"), not(isPresent()));
    }
  }

  static class TestClass {

    public static void main(String[] args) {
      System.out.println(new EscapesDirectly());
      System.out.println(new EscapesIndirectlySub());
      System.out.println(new EscapesIndirectlyWithOverrideSub());
      System.out.println(new DoesNotEscapeWithSubThatOverridesAndEscapesSub());

      new DoesNotEscape();
      new DoesNotEscapeWithSubThatDoesNotOverride();
      new DoesNotEscapeWithSubThatDoesNotOverrideSub();
      new DoesNotEscapeWithSubThatOverrides();
      new DoesNotEscapeWithSubThatOverridesSub();
      new DoesNotEscapeWithSubThatOverridesAndEscapes();
    }
  }

  static class EscapesDirectly {

    @Override
    public String toString() {
      return "EscapesDirectly";
    }
  }

  @NeverMerge
  static class EscapesIndirectly {

    @Override
    public String toString() {
      return "EscapesIndirectly";
    }
  }

  static class EscapesIndirectlySub extends EscapesIndirectly {}

  @NeverMerge
  static class EscapesIndirectlyWithOverride {

    @Override
    public String toString() {
      return "EscapesIndirectlyWithOverride";
    }
  }

  static class EscapesIndirectlyWithOverrideSub extends EscapesIndirectlyWithOverride {

    @Override
    public String toString() {
      return "EscapesIndirectlyWithOverrideSub";
    }
  }

  @NeverClassInline
  static class DoesNotEscape {

    @AssumeMayHaveSideEffects
    DoesNotEscape() {}

    @Override
    public String toString() {
      return "DoesNotEscape";
    }
  }

  @NeverClassInline
  static class DoesNotEscapeWithSubThatDoesNotOverride {

    @AssumeMayHaveSideEffects
    DoesNotEscapeWithSubThatDoesNotOverride() {}

    @Override
    public String toString() {
      return "DoesNotEscapeWithSubThatDoesNotOverride";
    }
  }

  @NeverClassInline
  static class DoesNotEscapeWithSubThatDoesNotOverrideSub
      extends DoesNotEscapeWithSubThatDoesNotOverride {

    @AssumeMayHaveSideEffects
    DoesNotEscapeWithSubThatDoesNotOverrideSub() {}
  }

  @NeverClassInline
  static class DoesNotEscapeWithSubThatOverrides {

    @AssumeMayHaveSideEffects
    DoesNotEscapeWithSubThatOverrides() {}

    @Override
    public String toString() {
      return "DoesNotEscapeWithSubThatOverrides";
    }
  }

  @NeverClassInline
  static class DoesNotEscapeWithSubThatOverridesSub extends DoesNotEscapeWithSubThatOverrides {

    @AssumeMayHaveSideEffects
    DoesNotEscapeWithSubThatOverridesSub() {}

    @Override
    public String toString() {
      return "DoesNotEscapeWithSubThatOverridesSub";
    }
  }

  // Note that this class and its subclass is equivalent to DoesNotEscapeWithSubThatOverrides and
  // DoesNotEscapeWithSubThatOverridesSub, respectively. The difference is that the class DoesNot-
  // EscapeWithSubThatOverridesAndEscapesSub is escaping from main(), unlike DoesNotEscapeWithSub-
  // ThatOverridesSub.
  @NeverClassInline
  static class DoesNotEscapeWithSubThatOverridesAndEscapes {

    @AssumeMayHaveSideEffects
    DoesNotEscapeWithSubThatOverridesAndEscapes() {}

    @Override
    public String toString() {
      return "DoesNotEscapeWithSubThatOverridesAndEscapes";
    }
  }

  @NeverClassInline
  static class DoesNotEscapeWithSubThatOverridesAndEscapesSub
      extends DoesNotEscapeWithSubThatOverridesAndEscapes {

    @AssumeMayHaveSideEffects
    DoesNotEscapeWithSubThatOverridesAndEscapesSub() {}

    @Override
    public String toString() {
      return "DoesNotEscapeWithSubThatOverridesAndEscapesSub";
    }
  }
}
