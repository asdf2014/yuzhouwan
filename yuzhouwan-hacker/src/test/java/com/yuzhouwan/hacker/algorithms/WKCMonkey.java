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

        0.24996		0.24996		0.24996		4.24996
        0.24996		0.24996		1.24996		3.24996
        0.24996		0.24996		2.24996		2.24996
        0.24996		1.24996		1.24996		2.24996
        1.24996		1.24996		1.24996		1.24996
     */
    @Test
    public void bestChoice() {
        makeTheBestChoice("24996");
        makeTheBestChoice("35340");
    }

    private void makeTheBestChoice(String monkeyId) {
        Integer v = Integer.valueOf(monkeyId);
        double feed = v / (Math.pow(10, monkeyId.length()));
        double max = 0, sum = 0;
        int count = 0, maxCount = 0;
        int pointBefore;
        double pointAfter;
        String monkeyIdStr = feed + "";
        String monkeyIdPointAfterStr = monkeyIdStr.substring(1, monkeyIdStr.length());
        while (sum < 5) {
            count++;
            if ((sum += feed) > 5) break;
            pointAfter = sum - (pointBefore = (int) sum);
            if (pointAfter > max) {
                max = pointAfter;
                maxCount = count;
            }
            System.out.println(String.format("max: %s, maxCount: %s, sum: %s, count: %s", max, maxCount, sum, count));
            System.out.println(String.format("pointBefore: %s, pointAfter: %s\n", pointBefore, pointAfter));
        }
        String result = theBest(4, maxCount, 0, new StringBuffer(), new StringBuffer()).toString();
        String[] split = result.split(",");
        for (String s : split) {
            if (s == null || s.length() == 0) continue;
            String[] single = s.split(" ");
            for (String single_ : single) {
                System.out.print(single_ + monkeyIdPointAfterStr + "\t\t");
            }
            System.out.println();
        }
    }

    private static StringBuffer theBest(int n, int m, int start, StringBuffer path, StringBuffer result) {
        if (n == 0) {
            result.append(path).append(",");
        }
        if (start <= n && m == 1) {
            path.append(n);
            result.append(path).append(",");
            path.setLength(path.length() - 1);
            return result;
        }
        for (int i = start; i <= n - 1; i++) {
            theBest(n - i, m - 1, i, path.append(i).append(" "), result);
            path.setLength(path.length() - 2);
        }
        return result;
    }
}
