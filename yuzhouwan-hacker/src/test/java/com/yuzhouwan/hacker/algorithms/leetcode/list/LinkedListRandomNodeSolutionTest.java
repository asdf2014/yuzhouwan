package com.yuzhouwan.hacker.algorithms.leetcode.list;

import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Linked List Random Node Solution Tester
 *
 * @author Benedict Jin
 * @since 2016/8/18
 */
public class LinkedListRandomNodeSolutionTest {

    /**
     * Your Solution object will be instantiated and called as such:
     * Solution obj = new Solution(head);
     * int param_1 = obj.getRandom();
     */
    @Test
    public void getRandomTest() throws Exception {

        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        LinkedListRandomNodeSolution l = new LinkedListRandomNodeSolution(head);

        // getRandom() should return either 1, 2, or 3 randomly.
        // Each element should have equal probability of returning.
        int count = 1000;
        while (count > 0) {
            count--;
            assertNotEquals(0, l.getRandom());
        }
    }
}
