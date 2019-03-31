package com.yuzhouwan.hacker.algorithms.leetcode.list;

import java.util.Random;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: Linked List Random Node Solution
 *
 * @author Benedict Jin
 * @since 2016/8/18
 */
class LinkedListRandomNodeSolution {

    /**
     * https://leetcode.com/problems/linked-list-random-node/
     * <p>
     * 382. Linked List Random Node
     * <p>
     * Given a singly linked list, return a random node's value from the linked list.
     * Each node must have the same probability of being chosen.
     * <p>
     * Follow up:
     * What if the linked list is extremely large and its length is unknown to you?
     * Could you solve this efficiently without using extra space?
     */
    private ListNode head;
    private Random random;

    /**
     * @param head The linked list's head.
     *             Note that the head is guaranteed to be not null, so it contains at least one node.
     */
    LinkedListRandomNodeSolution(ListNode head) {
        this.head = head;
        random = new Random();
    }

    /**
     * Returns a random node's value.
     */
    int getRandom() {

        ListNode now = head,
                result = head;

        for (int i = 1; now != null; i++) {
            if (random.nextInt(i) == 0) {
                result = now;
            }
            now = now.next;
        }
        return result.val;
    }
}
