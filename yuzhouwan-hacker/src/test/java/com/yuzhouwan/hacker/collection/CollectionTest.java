package com.yuzhouwan.hacker.collection;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Collection Test
 *
 * @author Benedict Jin
 * @since 2023/3/11
 */
@SuppressWarnings("ConstantConditions")
public class CollectionTest {

  /**
   * List.of 和 Collections.unmodifiableList 都可以用来创建不可修改的列表对象
   * 如果需要创建一个不可变的列表对象，并且知道列表的元素，那么 List.of 是一个更好的选择
   * 如果已经有一个可变的列表对象，并且需要将其转换为不可变的列表，则 Collections.unmodifiableList 是更好的选择
   */
  @Test
  public void testCollectionWithBasicType() {

    Assertions.assertThrows(UnsupportedOperationException.class, () -> {

      List<String> l = List.of("yuzhouwan.com");
      assertEquals(1, l.size());

      l.add("?");
      assertEquals(2, l.size());
    });

    Assertions.assertThrows(UnsupportedOperationException.class, () -> {

      List<String> l = new ArrayList<>();
      l.add("yuzhouwan");
      l.add(".com");
      l = Collections.unmodifiableList(l);
      assertEquals(2, l.size());

      l.add("?");
      assertEquals(3, l.size());
    });
  }

  /**
   * 当原始的集合变化之后，Collections.unmodifiableList 封装后的不可变集合，也会自动地相应修改
   */
  @Test
  public void testCollectionWithObject() {

    Assertions.assertThrows(UnsupportedOperationException.class, () -> {

      List<User> l = List.of(new User("yuzhouwan"), new User(".com"));
      assertEquals(2, l.size());
      l.get(0).setName("new");
      assertEquals("new", l.get(0).getName());

      l.add(new User("?"));
      assertEquals(2, l.size());
    });

    Assertions.assertThrows(UnsupportedOperationException.class, () -> {

      List<User> l = new ArrayList<>();
      l.add(new User("yuzhouwan"));
      l.add(new User(".com"));
      List<User> unmodifiable = Collections.unmodifiableList(l);
      assertEquals(2, l.size());
      assertEquals(2, unmodifiable.size());

      l.add(new User("/"));
      assertEquals(3, l.size());
      assertEquals(3, unmodifiable.size());

      unmodifiable.get(0).setName("new");
      assertEquals("new", unmodifiable.get(0).getName());

      unmodifiable.add(new User("?"));
      assertEquals(3, l.size());
    });
  }
}

class User {
  private String name;

  public User(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(name, user.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
