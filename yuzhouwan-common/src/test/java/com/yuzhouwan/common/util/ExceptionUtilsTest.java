package com.yuzhouwan.common.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Exception Utils Test
 *
 * @author Benedict Jin
 * @since 2016/11/24
 */
public class ExceptionUtilsTest {

  @Test
  public void errorInfo() {
    assertEquals("RuntimeException: Connection is closed!",
      ExceptionUtils.errorInfo(new RuntimeException("Connection is closed!")));

    assertEquals("RuntimeException: Connection is closed!",
      ExceptionUtils.errorInfo(new RuntimeException("Connection is closed!"), null));

    assertEquals("RuntimeException: Connection is closed, Detail: port is 31",
      ExceptionUtils.errorInfo(new RuntimeException("Connection is closed"), "port is 31"));
  }

  @Test
  public void errorPrint() {
    try {
      throw new RuntimeException("Runtime Exception...");
    } catch (Exception e) {
      assertEquals("Runtime Exception...", e.getMessage());
    }
  }
}
