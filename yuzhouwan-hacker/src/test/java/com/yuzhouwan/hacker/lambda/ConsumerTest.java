package com.yuzhouwan.hacker.lambda;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Consumer Test
 *
 * @author Benedict Jin
 * @since 2023/2/12
 */
public class ConsumerTest {

  @Test
  public void testAllMatch() {
    Stream<String> stream = Stream.of("https://", "yuzhouwan", ".com");
    Consumer<String> consumer = System.out::print;
    stream.forEach(consumer);

    stream = Stream.of("asdf", "2014", "yuzhouwan", ".", "com");
    List<String> l = new LinkedList<>();
    consumer = l::add;
    stream.forEach(consumer);
    assertEquals(5, l.size());
  }
}
