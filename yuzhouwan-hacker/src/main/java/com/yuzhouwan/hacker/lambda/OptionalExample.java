package com.yuzhouwan.hacker.lambda;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Optional Example
 *
 * @author Benedict Jin
 * @since 2023/2/4
 */
public class OptionalExample {

  public static void main(String[] args) {

    Optional<String> optionalEmpty = Optional.empty();
    System.out.println(optionalEmpty.orElse("yuzhouwan"));

    Optional<String> optionalStr = Optional.of("宇宙湾");
    optionalStr.ifPresent(System.out::println);

    Optional<String> optionalStr2 = Optional.of("asdf");
    System.out.println(optionalStr2.map(m -> m + "2014").orElse("1024"));

    Bean bean = new Bean(null);
    Optional<Bean> optionalBean = Optional.of(bean);
    optionalBean.map(Bean::getSet)
        .ifPresentOrElse(System.out::println,
            () -> System.err.println("The set in bean is null!")
        );

    optionalBean.ifPresent(b -> b.setSet(Collections.singleton("element")));
    optionalBean.map(Bean::getSet)
        .ifPresentOrElse(System.out::println,
            () -> System.err.println("The set in bean is null!")
        );
  }
}

class Bean {

  private Set<Object> set;

  public Bean(Set<Object> set) {
    this.set = set;
  }

  public Set<Object> getSet() {
    return set;
  }

  public void setSet(Set<Object> set) {
    this.set = set;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Bean bean = (Bean) o;
    return Objects.equals(set, bean.set);
  }

  @Override
  public int hashCode() {
    return Objects.hash(set);
  }
}
