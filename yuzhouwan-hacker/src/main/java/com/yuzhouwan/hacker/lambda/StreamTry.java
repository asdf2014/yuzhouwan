package com.yuzhouwan.hacker.lambda;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：lambda
 *
 * @author Benedict Jin
 * @since 2016/2/2 0002
 */
public class StreamTry {

    public static void main(String[] args) {

        List<String> words = new ArrayList<>(10);
        words.add("asdf");
        System.out.println(words.stream().allMatch(s -> s.equals("asdf")));
    }
}
