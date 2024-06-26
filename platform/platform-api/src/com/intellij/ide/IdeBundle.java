// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.ide;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.function.Supplier;

public final class IdeBundle {
  public static final String BUNDLE = "messages.IdeBundle";

  private static final DynamicBundle INSTANCE = new DynamicBundle(IdeBundle.class, BUNDLE);

  public static @NotNull @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, Object @NotNull ... params) {
    return INSTANCE.containsKey(key) ? INSTANCE.getMessage(key, params) : IdeDeprecatedMessagesBundle.message(key, params);
  }

  public static @NotNull Supplier<@Nls String> messagePointer(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, Object @NotNull ... params) {
    return INSTANCE.containsKey(key) ? INSTANCE.getLazyMessage(key, params) : IdeDeprecatedMessagesBundle.messagePointer(key, params);
  }

  private IdeBundle() {
  }

  @ApiStatus.Internal
  public static void clearCache() {
    INSTANCE.clearLocaleCache();
  }
}
