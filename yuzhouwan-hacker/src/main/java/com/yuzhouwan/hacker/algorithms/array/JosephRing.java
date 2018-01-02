package com.yuzhouwan.hacker.algorithms.array;

import java.util.LinkedList;
import java.util.List;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: Joseph Ring
 *
 * @author Benedict Jin
 * @since 2016/8/31
 */
public class JosephRing {

    public List<Integer> joseph(List<Integer> origin, int countNum) {

        List<Integer> result = new LinkedList<>();
        int k = 0;
        while (origin.size() > 0) {
            k = k + countNum;
            k = k % (origin.size()) - 1;
            if (k == -1) {
                result.add(origin.remove(origin.size() - 1)); //remove last elements
                k = 0;
            } else {
                result.add(origin.remove(k));
            }
        }
        return result;
    }
}
