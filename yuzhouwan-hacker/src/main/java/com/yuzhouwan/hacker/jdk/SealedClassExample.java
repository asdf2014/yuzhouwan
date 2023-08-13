package com.yuzhouwan.hacker.jdk;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Sealed class Example
 *
 * @author Benedict Jin
 * @since 2023/8/11
 */
public class SealedClassExample {

  public static void main(String[] args) {

  }
}

sealed strictfp class A permits B, C {

}

final class B extends A {

}

sealed class C extends A permits D {

}

non-sealed class D extends C {

}