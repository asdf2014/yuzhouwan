package com.yuzhouwan.hacker.algorithms.sort;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: Quick Sort Example
 *
 * @author Benedict Jin
 * @since 2016/9/19
 */
class QuickSortExample {

    static void quickSort(int[] n, int left, int right) {
        int dp;
        if (left < right) {
            dp = partition(n, left, right);
            quickSort(n, left, dp - 1);
            quickSort(n, dp + 1, right);
        }
    }

    private static int partition(int[] n, int left, int right) {
        int pivot = n[left];
        while (left < right) {
            while (left < right && n[right] >= pivot)
                right--;
            if (left < right)
                n[left++] = n[right];
            while (left < right && n[left] <= pivot)
                left++;
            if (left < right)
                n[right--] = n[left];
        }
        n[left] = pivot;
        return left;
    }
}
