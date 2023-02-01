package com.yuzhouwan.common.util;

import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: Random Utils
 *
 * @author Benedict Jin
 * @since 2016/8/23
 */
public final class RandomUtils {

    private RandomUtils() {
    }

    // return a real number uniformly between a and b
    public static double uniform(double a, double b) {
        return a + Math.random() * (b - a);
    }

    // return an integer uniformly between 0 and N-1
    public static int uniform(int n) {
        return (int) (Math.random() * n);
    }

    // return a boolean, which is true with prob p and false otherwise
    public static boolean bernoulli(double p) {
        return Math.random() < p;
    }

    // return a real number with a standard Gaussian distribution
    public static double gaussian() {
        double r, x, y;
        do {
            x = uniform(-1.0, 1.0);
            y = uniform(-1.0, 1.0);
            r = x * x + y * y;
        } while (r >= 1 || r == 0);
        return x * Math.sqrt(-2 * Math.log(r) / r);

        // Remark:  y * Math.sqrt(-2 * Math.log(r) / r)
        // is an independent random gaussian
    }

    // return a real number from a gaussian distribution with given mean and stddev
    public static double gaussian(double mean, double stddev) {
        return mean + stddev * gaussian();
    }

    public static int discrete(double[] a) {
        double x = Math.random();
        int i = 0;
        for (double sum = a[0]; x > sum; i++)
            sum += a[i + 1];
        return i;
    }

    // exponential random variable with rate lambda
    public static double exp(double lambda) {
        return -Math.log(1 - Math.random()) / lambda;
    }


    // Random permutation
    public static int[] perm(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        for (int i = 0; i < n; i++) {
            int r = i + (int) (Math.random() * (n - i));   // between i and N-1
            int temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
        return a;
    }

    // take as input an array of strings and rearrange them in random order
    public static void shuffle(String[] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            int r = i + (int) (Math.random() * (N - i));   // between i and N-1
            String temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    public static LinkedList<Integer> getGaussian(int start, int end, int num, long factor,
                                                  double stdDeviation, double variance, double mean) {
        LinkedList<Integer> gaussianList = new LinkedList<>();
        for (int i = start; i <= end; i++) {
            int y = (int) (getY(i, stdDeviation, variance, mean) * factor);
            gaussianList.add(y);
        }
        LinkedList<Integer> result = new LinkedList<>();
        if (end > num) {
            int step = (end - 1) / num;
            for (int i = 0; i < num; i++) {
                result.add(gaussianList.get(i * step));
            }
        } else if (end == num) {
            for (int i = 0; i < num; i++) result.add(gaussianList.get(i));
        } else {
            int step = num / end + 1;
            for (int i = 0; i < end && result.size() < num; i++) {
                int y = gaussianList.get(i);
                int subY = y / step;
                for (int k = 0; k < step; k++) result.add(subY);
            }
        }
        return result;
    }

    public static double getY(double x, double stdDeviation, double variance, double mean) {
        return Math.pow(Math.exp(-(((x - mean) * (x - mean)) / ((2 * variance)))),
                1 / (stdDeviation * Math.sqrt(2 * Math.PI)));
    }

    public static long uuid() {
        return System.nanoTime() - ThreadLocalRandom.current().nextLong();
    }

    public static String uuid2() {
        return UUID.randomUUID().toString();
    }
}
