package com.yuzhouwan.hacker.basic;

import org.junit.Test;

import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：ZoneOffset Example
 *
 * @author Benedict Jin
 * @since 2023/7/9
 */
public class ZoneOffsetExample {

  @Test
  public void testUTC() {
    assertEquals("Z", ZoneOffset.UTC.getId());
    assertEquals(ZoneOffset.UTC.toString(), ZoneOffset.UTC.getId());
  }
}
