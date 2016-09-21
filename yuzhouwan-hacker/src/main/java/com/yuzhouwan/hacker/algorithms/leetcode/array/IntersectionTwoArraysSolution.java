package com.yuzhouwan.hacker.algorithms.leetcode.array;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Intersection of Two Arrays Solution
 *
 * @author Benedict Jin
 * @since 2016/9/20
 */
class IntersectionTwoArraysSolution {

    /**
     * https://leetcode.com/problems/intersection-of-two-arrays/
     *
     * 349. Intersection of Two Arrays
     *
     * Given two arrays, write a function to compute their intersection.
     *
     * Example:
     * Given nums1 = [1, 2, 2, 1], nums2 = [2, 2], return [2].
     *
     * Note:
     * Each element in the result must be unique.
     * The result can be in any order.
     */
    static int[] intersection(int[] nums1, int[] nums2) {

        Set<Integer> set1 = new HashSet<>();
        Set<Integer> set2 = new HashSet<>();
        for (int i : nums1) {
            set1.add(i);
        }
        for (int i : nums2) {
            if (set1.contains(i))
                set2.add(i);
        }
        int len = set2.size();
        int[] result = new int[len];
        if (len == 0) return result;
        Iterator<Integer> iter = set2.iterator();
        for (int i = 0; i < len; i++) {
            result[i] = iter.next();
        }
        return result;
    }
}
