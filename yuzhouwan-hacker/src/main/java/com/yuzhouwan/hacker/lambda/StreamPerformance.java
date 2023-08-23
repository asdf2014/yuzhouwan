package com.yuzhouwan.hacker.lambda;

import java.util.ArrayList;
import java.util.Random;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Stream Performance
 *
 * @author Benedict Jin
 * @since 2017/5/2
 */
public class StreamPerformance {

    private static final Random r = new Random();

    public static void main(String[] args) {
        // -Xmx2G -Xms2G -Xmn1G -XX:+AlwaysPreTouch
        /*
        Simple Loop:
            ArrayList: 1 elements, Count: 0, Time: 25193 ns
        Lambda Stream:
            ArrayList: 1 elements, Count: 0, Time: 19283 ns
        Simple Loop:
            ArrayList: 10 elements, Count: 26, Time: 3111 ns
        Lambda Stream:
            ArrayList: 10 elements, Count: 26, Time: 8398 ns
        Simple Loop:
            ArrayList: 100 elements, Count: 2589, Time: 21149 ns
        Lambda Stream:
            ArrayList: 100 elements, Count: 2589, Time: 26126 ns
        Simple Loop:
            ArrayList: 1000 elements, Count: 254389, Time: 146490 ns
        Lambda Stream:
            ArrayList: 1000 elements, Count: 254389, Time: 164218 ns
        Simple Loop:
            ArrayList: 10000 elements, Count: 24861987, Time: 742710 ns
        Lambda Stream:
            ArrayList: 10000 elements, Count: 24861987, Time: 471192 ns
         */
        performance(0);     // initialization resource
        performance(1);
        performance(10);
        performance(100);
        performance(1000);
        performance(1_0000);
    }

    private static void performance(int len) {
        ArrayList<Integer> list = new ArrayList<>(len);
        for (int i = 1; i <= len; i++) {
            list.add(r.nextInt(i));
        }
        int countLoop = 0, countStream = 0;
        long startTime, endTime;

        startTime = System.nanoTime();
        for (Integer i : list) {
            countLoop += i;
        }
        endTime = System.nanoTime();
        System.out.printf("Simple Loop:\r\n\tArrayList: %d elements, Count: %d, Time: %d ns%n",
                len, countLoop, (endTime - startTime));

        startTime = System.nanoTime();
        countStream = list.stream().reduce(countStream, Integer::sum);
        endTime = System.nanoTime();

        System.out.printf("Lambda Stream:\r\n\tArrayList: %d elements, Count: %d, Time: %d ns%n",
                len, countStream, (endTime - startTime));
        list.clear();
    }
}
