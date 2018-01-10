package com.yuzhouwan.hacker.algorithms;

import org.junit.Test;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：WKC Monkey
 *
 * @author Benedict Jin
 * @since 2018/1/10
 */
public class WKCMonkey {

    /*
        max: 0.99984, maxCount: 4.0, sum: 0.99984, count: 4.0
        pointBefore: 0, pointAfter: 0.99984
        max: 0.99984, maxCount: 4.0, sum: 4.9992, count: 20.0
        pointBefore: 4, pointAfter: 0.9992000000000001

        4 = 4 + 0 + 0 + 0
        4 = 3 + 1 + 0 + 0
        4 = 2 + 1 + 1 + 0
        4 = 2 + 2 + 0 + 0
        4 = 1 + 1 + 1 + 1
        4.24996 / 0.24996 / 0.24996 / 0.24996
        3.24996 / 1.24996 / 0.24996 / 0.24996
        2.24996 / 1.24996 / 1.24996 / 0.24996
        2.24996 / 2.24996 / 0.24996 / 0.24996
        1.24996 / 1.24996 / 1.24996 / 1.24996
     */
    @Test
    public void bestChoice() {
        double monkeyId = 0.24996, max = 0, sum = 0, count = 0, maxCount = 0;
        int pointBefore;
        double pointAfter;
        while (sum < 5) {
            count++;
            sum += monkeyId;
            if (sum > 5) break;
            pointAfter = sum - (pointBefore = (int) sum);
            if (pointAfter > max) {
                max = pointAfter;
                maxCount = count;
            }
            System.out.println(String.format("max: %s, maxCount: %s, sum: %s, count: %s", max, maxCount, sum, count));
            System.out.println(String.format("pointBefore: %s, pointAfter: %s\n", pointBefore, pointAfter));
        }
    }
}
