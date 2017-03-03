package com.yuzhouwan.common.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: Random Utils
 *
 * @author Benedict Jin
 * @since 2016/8/23
 */
public class RandomUtils {

    // return a real number uniformly between a and b
    public static double uniform(double a, double b) {
        return a + Math.random() * (b - a);
    }

    // return an integer uniformly between 0 and N-1
    public static int uniform(int N) {
        return (int) (Math.random() * N);
    }

    // return a boolean, which is true with prob p and false otherwise
    public static boolean bernoulli(double p) {
        return Math.random() < p;
    }

    // return a real number with a standard Gausian distribution
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
    public static int[] perm(int N) {
        int[] a = new int[N];
        for (int i = 0; i < N; i++) a[i] = i;
        for (int i = 0; i < N; i++) {
            int r = i + (int) (Math.random() * (N - i));   // between i and N-1
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

    public static void main(String[] args) {
        int N = 11;
        int[] a = perm(N);
        for (int i = 0; i < N; i++)
            System.out.print(a[i] + " ");
        System.out.println();

        for (int i = 1; i <= N; i++)
            System.out.print((int) gaussian(1.5, i) + " ");
        System.out.println();

        Random r = new Random();
        LinkedList<Double> l = new LinkedList<>();
        for (int i = 0; i < N; i++) {
            double g = r.nextGaussian();
            l.add(g);
            System.out.print(g + " ");
        }
        System.out.println();
        for (double d : l)
            System.out.print((int) (d * 10) + " ");
        System.out.println();
        Collections.sort(l);
        for (Double d : l)
            System.out.print(d + " ");
        System.out.println();

        int threadShort = 3, threadLong = 70;
        int total = 0;
        int time = 15;
        LinkedList<Integer> gaussianList = new LinkedList<>();
        LinkedList<Integer> gaussianListShort = new LinkedList<>();
        LinkedList<Integer> gaussianListLong = new LinkedList<>();
        for (int i = 1; i <= time; i++) {
            int y = (int) (getY(i) * 1500);
            gaussianList.add(y);
            total += y;
            System.out.print(y + " ");
        }
        System.out.println();
        System.out.println("total: " + total);
        System.out.println();

        for (int i = 0; i < threadShort; i++) {
            gaussianListShort.add(gaussianList.get(i));
        }
        int step = threadLong / time + 1;
        for (int i = 0; i < time && gaussianListLong.size() < threadLong; i++) {
            int y = gaussianList.get(i);
            int subY = y / step;
            for (int k = 0; k < step; k++) {
                gaussianListLong.add(subY);
            }
        }
        total = 0;
        for (Integer iShort : gaussianListShort) {
            total += iShort;
            System.out.print(iShort + " ");
        }
        System.out.println();
        System.out.println("Short Total:" + total + ", Length: " + gaussianListShort.size());
        total = 0;
        for (Integer iLong : gaussianListLong) {
            total += iLong;
            System.out.print(iLong + " ");
        }
        System.out.println();
        System.out.println("Long Total:" + total + ", Length: " + gaussianListLong.size());
    }

    public static LinkedList<Integer> getGaussian(int start, int end, int num) {
        LinkedList<Integer> gaussianList = new LinkedList<>();
        LinkedList<Integer> result = new LinkedList<>();
        for (int i = start; i <= end; i += 1) {
            int y = (int) (getY(i) * 6000);
            gaussianList.add(y);
        }
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

    public static double getY(double x) {
        double stdDeviation = 0.7;
        double variance = 1;
        double mean = 4;
        return Math.pow(Math.exp(-(((x - mean) * (x - mean)) / ((2 * variance)))), 1 / (stdDeviation * Math.sqrt(2 * Math.PI)));
    }
}