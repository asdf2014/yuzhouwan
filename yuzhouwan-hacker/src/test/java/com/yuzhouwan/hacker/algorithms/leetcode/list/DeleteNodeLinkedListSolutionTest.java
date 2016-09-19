package com.yuzhouwan.hacker.algorithms.leetcode.list;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Delete Node in a Linked List Solution Tester
 *
 * @author Benedict Jin
 * @since 2016/9/13
 */
public class DeleteNodeLinkedListSolutionTest {

    @Test
    public void delete() throws Exception {

        ListNode listNode0 = new ListNode(0);
        ListNode listNode1 = new ListNode(1);
        ListNode listNode2 = new ListNode(2);
        listNode0.next = listNode1;
        listNode1.next = listNode2;
        DeleteNodeLinkedListSolution.deleteNode(listNode1);
        assertEquals(true, listNode0.next.val == 2);
    }
}
