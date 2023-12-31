package com.yuzhouwan.hacker.jdk;

import java.util.Arrays;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Sealed class Example
 *
 * @author Benedict Jin
 * @since 2023/8/11
 */
public class SealedClassExample {

  public static void main(String[] args) {
    // class com.yuzhouwan.hacker.jdk.A
    System.out.println(A.class);
    // class com.yuzhouwan.hacker.jdk.B
    System.out.println(B.class);
    // class com.yuzhouwan.hacker.jdk.C
    System.out.println(C.class);
    // class com.yuzhouwan.hacker.jdk.D
    System.out.println(D.class);
    // class com.yuzhouwan.hacker.jdk.E
    System.out.println(E.class);

    // [class com.yuzhouwan.hacker.jdk.B, class com.yuzhouwan.hacker.jdk.C]
    System.out.println(Arrays.toString(A.class.getPermittedSubclasses()));
    // null
    System.out.println(Arrays.toString(B.class.getPermittedSubclasses()));
    // [class com.yuzhouwan.hacker.jdk.D, class com.yuzhouwan.hacker.jdk.E]
    System.out.println(Arrays.toString(C.class.getPermittedSubclasses()));
    // null
    System.out.println(Arrays.toString(D.class.getPermittedSubclasses()));
    // null
    System.out.println(Arrays.toString(E.class.getPermittedSubclasses()));
  }
}

sealed strictfp class A permits B, C {

}

final class B extends A {

}

sealed class C extends A permits D, E {

}

non-sealed abstract class D extends C {

}

non-sealed class E extends C {

}
