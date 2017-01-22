package com.yuzhouwan.hacker.algorithms.tree.RedBlackTree;

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
    }

} 