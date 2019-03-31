package com.yuzhouwan.hacker.algorithms;

import org.junit.jupiter.api.Test;

import static com.yuzhouwan.common.util.StrUtils.isBlank;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：WKC Monkey
 *
 * @author Benedict Jin
 * @since 2018/1/10
 */
public class WKCMonkey {

    private static final int MAX_FEED = 5;
    private static final int MAX_COUNT = 10;
    private static final String HIGH_SPLITTER = ",";
    private static final String LOW_SPLITTER = " ";

    private static StringBuffer theBest(int n, int m, int start, StringBuffer path, StringBuffer result) {
        if (n == 0) result.append(path).append(HIGH_SPLITTER);
        if (start <= n && m == 1) {
            path.append(n);
            result.append(path).append(HIGH_SPLITTER);
            path.setLength(path.length() - 1);
            return result;
        }
        for (int i = start; i <= n - 1; i++) {
            theBest(n - i, m - 1, i, path.append(i).append(LOW_SPLITTER), result);
            path.setLength(path.length() - 2);
        }
        return result;
    }

    /*
        max: 0.99984, maxCount: 4.0, sum: 0.99984, count: 4.0
        pointBefore: 0, pointAfter: 0.99984
        max: 0.99984, maxCount: 4.0, sum: 4.9992, count: 20.0
        pointBefore: 4, pointAfter: 0.9992000000000001

        0.24996		0.24996		0.24996		4.24996
        0.24996		0.24996		1.24996		3.24996
        0.24996		0.24996		2.24996		2.24996
        0.24996		1.24996		1.24996		2.24996
        1.24996		1.24996		1.24996		1.24996
     */
    @Test
    public void bestChoice() {
        makeTheBestChoice("24996");
        makeTheBestChoice("200004");
    }

    private void makeTheBestChoice(String monkeyId) {
        double feed = Integer.valueOf(monkeyId) / (Math.pow(10, monkeyId.length()));
        double max = 0, sum = 0;
        int count = 0, maxCount = 0;
        int pointBefore, maxPointBefore = 0;
        double pointAfter;
        String monkeyIdStr = feed + "";
        String monkeyIdPointAfterStr = monkeyIdStr.substring(1);
        while (sum < MAX_FEED) {
            count++;
            if (count > MAX_COUNT || (sum += feed) > MAX_FEED) break;
            pointAfter = sum - (pointBefore = (int) sum);
            if (pointAfter > max) {
                maxPointBefore = pointBefore;
                max = pointAfter;
                maxCount = count;
            }
            System.out.println(String.format("max: %s, maxCount: %s, sum: %s, count: %s", max, maxCount, sum, count));
            System.out.println(String.format("pointBefore: %s, pointAfter: %s\n", pointBefore, pointAfter));
        }
        String result = theBest(MAX_FEED - 1 - maxPointBefore, maxCount, 0, new StringBuffer(), new StringBuffer()).toString();
        String[] split = result.split(HIGH_SPLITTER);
        for (String s : split) {
            if (isBlank(s)) continue;
            String[] single = s.split(LOW_SPLITTER);
            for (String _single : single) {
                System.out.print(_single + monkeyIdPointAfterStr + "\t\t");
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }
}
