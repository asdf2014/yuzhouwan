package com.yuzhouwan.hacker.algorithms;

/**
 * Created by Benedict Jin on 2015/9/23.
 */
public class MergeSort {

    public int[] transfer(int[] benefits) {

        int bLen = benefits.length;

        if (bLen < 2)
            throw new ArithmeticException("Need more than two numbers.");

        int[] transfer = new int[bLen - 1];
        for (int i = 0; i < bLen - 1; i++) {
            transfer[i] = benefits[i + 1] - benefits[i];
        }

        return transfer;
    }

    public int maxBenefit(int[] benefits, int low, int mid, int high) {

        int leftSum = 0;

        int sum1 = 0;
        for (int i = mid; i >= low; i--) {
            sum1 += benefits[i];
            if (sum1 > leftSum) {
                leftSum = sum1;
            }
        }

        int rightSum = 0;

        int sum2 = 0;
        for (int j = mid + 1; j <= high; j++) {
            sum2 = sum2 + benefits[j];
            if (sum2 > rightSum) {
                rightSum = sum2;
            }
        }

        return leftSum + rightSum;
    }

    public int findMaximum(int[] benefits, int low, int high) {

        int len = benefits.length;
        if (len <= 0)
            throw new ArithmeticException("Need more than one number in array.");

        if (low < 0 || low > len - 1 || high < 0 || high > len - 1 || low > high)
            throw new ArithmeticException("0 <= low <= high < arr.length");

        int maxLeft = 0, maxMiddle = 0, maxRight = 0;
        if (low == high)
            return benefits[low];
        else {
            int mid = (low + high) / 2;
            maxLeft = findMaximum(benefits, low, mid);
            maxMiddle = maxBenefit(benefits, low, mid, high);
            maxRight = findMaximum(benefits, mid + 1, high);

            if (maxLeft > maxMiddle && maxLeft > maxRight)
                return maxLeft;
            else if (maxMiddle > maxLeft && maxMiddle > maxRight)
                return maxMiddle;
            else
                return maxRight;
        }
    }

}
