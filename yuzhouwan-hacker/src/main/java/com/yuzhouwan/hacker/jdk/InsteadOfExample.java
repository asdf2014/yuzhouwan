package com.yuzhouwan.hacker.jdk;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Instead of Example
 *
 * @author Benedict Jin
 * @since 2023/8/9
 */
@SuppressWarnings({"ConstantConditions",
    "PatternVariableCanBeUsed",
    "CastCanBeRemovedNarrowingVariableType"})
public class InsteadOfExample {

  public static void main(String[] args) {

    Object obj = "yuzhouwan";

    // Old code
    if (obj instanceof String) {
      String s = (String) obj;
      System.out.println(s);
    }

    // New code
    if (obj instanceof String s) {
      System.out.println(s);
    }
  }
}
