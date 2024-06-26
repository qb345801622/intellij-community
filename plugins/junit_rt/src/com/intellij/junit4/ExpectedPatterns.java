// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.junit4;

import com.intellij.rt.execution.junit.ComparisonFailureData;
import com.intellij.rt.execution.testFrameworks.AbstractExpectedPatterns;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ExpectedPatterns extends AbstractExpectedPatterns {
  private static final List<Pattern> PATTERNS = new ArrayList<>();

  private static final String[] PATTERN_STRINGS = new String[]{
    "\nexpected: is \"(.*)\"\n\\s*got: \"(.*)\"\n",
    "\nexpected: is \"(.*)\"\n\\s*but:\\s+was \"(.*)\"",
    "\nexpected: (.*)\n\\s*got: (.*)",
    "expected same:<(.*)> was not:<(.*)>",
    "\nexpected: \"(.*)\"\n\\s*but: was \"(.*)\"",
    "expected: (.*)\n\\s*but: was (.*)",
    "expected: (.*)\\s+but was: (.*)",
    "expecting:\\s+<(.*)> to be equal to:\\s+<(.*)>\\s+but was not"
  };

  private static final String JUNIT_FRAMEWORK_COMPARISON_NAME = "junit.framework.ComparisonFailure";

  private static final String ORG_JUNIT_COMPARISON_NAME = "org.junit.ComparisonFailure";

  static {
    registerPatterns(PATTERN_STRINGS, PATTERNS);
  }

  public static ComparisonFailureData createExceptionNotification(String message) {
    return createExceptionNotification(message, PATTERNS);
  }

  public static ComparisonFailureData createExceptionNotification(Throwable assertion) {
    if (isComparisonFailure(assertion)) {
      return ComparisonFailureData.create(assertion);
    }
    try {
      final Throwable cause = assertion.getCause();
      if (isComparisonFailure(cause)) {
        return ComparisonFailureData.create(cause);
      }
    }
    catch (Throwable ignore) {
    }

    final String message = assertion.getMessage();
    if (message != null) {
      return createExceptionNotification(message);
    }
    return null;
  }

  private static boolean isComparisonFailure(Throwable throwable) {
    if (throwable == null) return false;
    return isComparisonFailure(throwable.getClass());
  }

  private static boolean isComparisonFailure(Class<?> aClass) {
    if (aClass == null) return false;
    final String throwableClassName = aClass.getName();
    if (throwableClassName.equals(JUNIT_FRAMEWORK_COMPARISON_NAME) ||
        throwableClassName.equals(ORG_JUNIT_COMPARISON_NAME) ||
        throwableClassName.equals(ComparisonFailureData.OPENTEST4J_ASSERTION)) {
      return true;
    }
    return isComparisonFailure(aClass.getSuperclass());
  }
}
