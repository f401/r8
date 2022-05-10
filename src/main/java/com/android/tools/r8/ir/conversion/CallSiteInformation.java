// Copyright (c) 2017, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.ir.conversion;

import com.android.tools.r8.graph.AppView;
import com.android.tools.r8.graph.DexMethod;
import com.android.tools.r8.graph.ImmediateProgramSubtypingInfo;
import com.android.tools.r8.graph.ProgramMethod;
import com.android.tools.r8.ir.conversion.CallGraph.Node;
import com.android.tools.r8.shaking.AppInfoWithLiveness;
import com.android.tools.r8.utils.classhierarchy.MethodOverridesCollector;
import com.android.tools.r8.utils.collections.ProgramMethodSet;
import com.google.common.collect.Sets;
import java.util.Set;

public abstract class CallSiteInformation {

  /**
   * Check if the <code>method</code> is guaranteed to only have a single call site.
   *
   * <p>For pinned methods (methods kept through Proguard keep rules) this will always answer <code>
   * false</code>.
   */
  public abstract boolean hasSingleCallSite(ProgramMethod method);

  public abstract boolean hasDoubleCallSite(ProgramMethod method);

  public abstract void unsetCallSiteInformation(ProgramMethod method);

  public static CallSiteInformation empty() {
    return EmptyCallSiteInformation.EMPTY_INFO;
  }

  private static class EmptyCallSiteInformation extends CallSiteInformation {

    private static final EmptyCallSiteInformation EMPTY_INFO = new EmptyCallSiteInformation();

    @Override
    public boolean hasSingleCallSite(ProgramMethod method) {
      return false;
    }

    @Override
    public boolean hasDoubleCallSite(ProgramMethod method) {
      return false;
    }

    @Override
    public void unsetCallSiteInformation(ProgramMethod method) {
      // Intentionally empty.
    }
  }

  static class CallGraphBasedCallSiteInformation extends CallSiteInformation {

    private final Set<DexMethod> singleCallSite = Sets.newIdentityHashSet();
    private final Set<DexMethod> doubleCallSite = Sets.newIdentityHashSet();

    CallGraphBasedCallSiteInformation(AppView<AppInfoWithLiveness> appView, CallGraph graph) {
      ProgramMethodSet pinned =
          MethodOverridesCollector.findAllMethodsAndOverridesThatMatches(
              appView,
              ImmediateProgramSubtypingInfo.create(appView),
              appView.appInfo().classes(),
              method ->
                  appView.getKeepInfo(method).isPinned(appView.options())
                      || appView.appInfo().isMethodTargetedByInvokeDynamic(method));

      for (Node node : graph.nodes) {
        ProgramMethod method = node.getProgramMethod();
        DexMethod reference = method.getReference();

        // For non-pinned methods and methods that override library methods we do not know the exact
        // number of call sites.
        if (pinned.contains(method)) {
          continue;
        }

        if (appView.options().inlinerOptions().disableInliningOfLibraryMethodOverrides
            && method.getDefinition().isLibraryMethodOverride().isTrue()) {
          continue;
        }

        if (method.getDefinition().isDefaultInitializer()
            && appView.hasProguardCompatibilityActions()
            && appView.getProguardCompatibilityActions().isCompatInstantiated(method.getHolder())) {
          continue;
        }

        int numberOfCallSites = node.getNumberOfCallSites();
        if (numberOfCallSites == 1) {
          singleCallSite.add(reference);
        } else if (numberOfCallSites == 2) {
          doubleCallSite.add(reference);
        }
      }
    }

    /**
     * Checks if the given method only has a single call site.
     *
     * <p>For pinned methods (methods kept through Proguard keep rules) and methods that override a
     * library method this always returns false.
     */
    @Override
    public boolean hasSingleCallSite(ProgramMethod method) {
      return singleCallSite.contains(method.getReference());
    }

    /**
     * Checks if the given method only has two call sites.
     *
     * <p>For pinned methods (methods kept through Proguard keep rules) and methods that override a
     * library method this always returns false.
     */
    @Override
    public boolean hasDoubleCallSite(ProgramMethod method) {
      return doubleCallSite.contains(method.getReference());
    }

    @Override
    public void unsetCallSiteInformation(ProgramMethod method) {
      singleCallSite.remove(method.getReference());
      doubleCallSite.remove(method.getReference());
    }
  }
}