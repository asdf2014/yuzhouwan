package com.yuzhouwan.hacker.algorithms.leetcode.list;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function: Delete Node in a Linked List Solution
 *
 * @author Benedict Jin
 * @since 2016/9/13
 */
class DeleteNodeLinkedListSolution {

    static void deleteNode(ListNode node) {
        ListNode next = node.next;
        node.val = next.val;
        node.next = next.next;
    }
}
