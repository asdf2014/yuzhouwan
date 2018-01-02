package com.yuzhouwan.hacker.algorithms.tree.RedBlackTree;

import com.yuzhouwan.common.util.RandomUtils;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: RedBlackBST Client
 *
 * @author Benedict Jin
 * @since 2016/8/23
 */
public class RedBlackBSTApp {

    public static void main(String[] args) throws InterruptedException {

        int N = 11;
        RedBlackBST<Integer, Integer> st = new RedBlackBST<>(1);
        int[] a = RandomUtils.perm(N);
        double[] xpl = new double[N];
        double[] htB = new double[N];
        double htBold = 1;

        for (int i = 0; i < N; i++) {
            st.put(a[i], i);
            if (st.heightB() != htBold) {
                htBold = st.heightB();
                System.out.print(i + " ");
            }
            if (N < 1111) {
                RBPainter.setPenRadius(.0025);
                RBPainter.clear(RBPainter.LIGHT_GRAY);
                st.draw(.95, .003, .004);
                xpl[i] = (1.0 * st.ipl() + 2.0 * (i + 1)) / (i + 2);
                htB[i] = st.heightB();
                for (int j = 0; j <= i; j++) {
                    double x = (0.5 + j) / (i + 1);
                    double scale = 40.0;
                    RBPainter.setPenRadius(.003);
                    RBPainter.setPenColor(RBPainter.RED);
                    RBPainter.line(x, 0.0, x, xpl[j] / scale);
                    RBPainter.setPenColor(RBPainter.BLACK);
                    RBPainter.line(x, 0.0, x, htB[j] / scale);
                    RBPainter.setPenColor(RBPainter.RED);
                    RBPainter.line(x, 0.3, x, 0.3 + (xpl[j] - htB[j]) / scale);
                }
                // debug here for slow down
                RBPainter.show(100);
            }
        }
        double REDpl = (1.0 * st.ipl() + 2.0 * (N)) / (N + 1) - st.heightB();
        System.out.println();
        System.out.println("       " + (st.ipl() + 2 * N) + "\t" + 1.0 * st.sizeRed() / N);
        System.out.println("       " + st.heightB() + "\t" + REDpl);
    }

}
