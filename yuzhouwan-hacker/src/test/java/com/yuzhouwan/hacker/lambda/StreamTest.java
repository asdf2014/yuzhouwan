package com.yuzhouwan.hacker.lambda;

import org.junit.Assert;
import org.junit.Test;

import java.util.stream.Stream;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Optional Example
 *
 * @author Benedict Jin
 * @since 2023/2/4
 */
public class StreamTest {

  @Test
  public void testAllMatch() {
    Assert.assertTrue(Stream.empty().allMatch(e -> e == e));
    Assert.assertTrue(Stream.empty().allMatch(e -> e == "yuzhouwan.com"));
  }
}
