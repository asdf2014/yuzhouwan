package com.yuzhouwan.site.hacker.commons;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: Collection Stuff
 *
 * @author Benedict Jin
 * @since 2016/3/11 0030
 */
public class CollectionWithMethod {

    public void doSomethingMethod() {
        List<Integer> l = new ArrayList<>();
        something(l);
        System.out.println(l.size());
    }

    public void something(List<Integer> l) {
        l.add(1);
    }

}
