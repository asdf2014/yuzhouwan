package com.yuzhouwan.site.hacker.algorithms;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * StrassenMatrix Tester.
 *
 * @author <asdf2014>
 * @version 1.0
 * @since <pre>十月 8, 2015</pre>
 */
public class StrassenMatrixTest {

    private static StrassenMatrix sm;

    @Before
    public void before() throws Exception {
        sm = new StrassenMatrix();
    }

    @After
    public void after() throws Exception {
        sm = null;
    }


    @Test
    public void strassenAddTest() {
        {
            double[][] matrixA = {{1, 1}, {1, 1}};
            double[][] matrixB = {{0, 1}, {2, 3}};
            for (double[] row : sm.minMatrixAdd(matrixA, matrixB, 0, 1, 0, 1, 2, 2)) {
                for (double col : row) {
                    System.out.print(col + "\t");
                }
                System.out.print("\r\n");
            }
        }
        System.out.println("-----------------------------");
        {
            double[][] matrixA = {{1}, {1}};
            double[][] matrixB = {{1}, {1}};
            for (double[] row : sm.minMatrixAdd(matrixA, matrixB, 0, 1, 0, 1, 2, 2)) {
                for (double col : row) {
                    System.out.print(col + "\t");
                }
                System.out.print("\r\n");
            }
        }
        System.out.println("-----------------------------");
        {
            double[][] matrixA = {{0, 0}};
            double[][] matrixB = {{1, 1}};
            for (double[] row : sm.minMatrixAdd(matrixA, matrixB, 0, 1, 0, 1, 2, 2)) {
                for (double col : row) {
                    System.out.print(col + "\t");
                }
                System.out.print("\r\n");
            }
        }
    }

    @Test
    public void strassenMultiplyTest() {
        {
            double[][] matrixA = {{1, 1}, {1, 0}};
            double[][] matrixB = {{0, 1}, {1, 0}};
            for (double[] row : sm.minMatrixMultiply(matrixA, matrixB, 0, 1, 0, 1, 2, 2)) {
                for (double col : row) {
                    System.out.print(col + "\t");
                }
                System.out.print("\r\n");
            }
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        {
            double[][] matrixA = {{1, 1}, {1, 0}};
            double[][] matrixB = {{0, 1, 0}, {1, 0, 1}};
            for (double[] row : sm.minMatrixMultiply(matrixA, matrixB, 0, 1, 0, 1, 2, 2)) {
                for (double col : row) {
                    System.out.print(col + "\t");
                }
                System.out.print("\r\n");
            }
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        {
            double[][] matrixA = {{1}, {1}};
            double[][] matrixB = {{1, 1}};
            for (double[] row : sm.minMatrixMultiply(matrixA, matrixB, 0, 1, 0, 1, 2, 2)) {
                for (double col : row) {
                    System.out.print(col + "\t");
                }
                System.out.print("\r\n");
            }
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        {
            double[][] matrixA = {{1}, {1}};
            double[][] matrixB = {{1, 1}};
            for (double[] row : sm.minMatrixMultiply(matrixB, matrixA, 0, 1, 0, 1, 2, 2)) {
                for (double col : row) {
                    System.out.print(col + "\t");
                }
                System.out.print("\r\n");
            }
        }
    }

    @Test
    public void violent() {

        int[][] matrixA = {{1, 1, 1}, {0, 1, 0}};
        int[][] matrixB = {{0, 0}, {1, 1}, {0, 1}};

        matrixProduct(matrixA, matrixB);
    }

    private void matrixProduct(int[][] matrixA, int[][] matrixB) {
        if (matrixA.length < 1) {
            throw new RuntimeException("Cannot input a empty matrix");
        }
        int sameSize = matrixA[0].length;
        int sameSize2 = matrixB.length;
        if (sameSize != sameSize2) {
            throw new MathArithmeticException();
        }

        int rowSize = matrixA.length;
        int cloSize = matrixB[0].length;
        int[][] matrixC = new int[rowSize][cloSize];

        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < cloSize; j++) {
                int subSum = 0;
                for (int k = 0; k < sameSize; k++) {
                    subSum += matrixA[i][k] * matrixB[k][j];
                }
                matrixC[i][j] = subSum;
            }
        }
        for (int[] line : matrixC) {
            for (int element : line) {
                System.out.print(element + "\t");
            }
            System.out.print("\r\n");
        }
    }

    @Test
    public void simpleStrassen() {

        int[][] matrixA = {{1, 1}, {1, 0}};
        int[][] matrixB = {{0, 1}, {1, 0}};

        int s1 = matrixB[0][1] - matrixB[1][1];
        int s2 = matrixA[0][0] + matrixA[0][1];
        int s3 = matrixA[1][0] + matrixA[1][1];
        int s4 = matrixB[1][0] - matrixB[0][0];
        int s5 = matrixA[0][0] + matrixA[1][1];
        int s6 = matrixB[0][0] + matrixB[1][1];
        int s7 = matrixA[0][1] - matrixA[1][1];
        int s8 = matrixB[1][0] + matrixB[1][1];
        int s9 = matrixA[0][0] - matrixA[1][0];
        int s10 = matrixB[0][0] + matrixB[0][1];

        int p1 = matrixA[0][0] * s1;
        int p2 = s2 * matrixB[1][1];
        int p3 = s3 * matrixB[0][0];
        int p4 = matrixA[1][1] * s4;
        int p5 = s5 * s6;
        int p6 = s7 * s8;
        int p7 = s9 * s10;

        int[][] matrixC = new int[2][2];
        matrixC[0][0] = p5 + p4 - p2 + p6;
        matrixC[0][1] = p1 + p2;
        matrixC[1][0] = p3 + p4;
        matrixC[1][1] = p5 + p1 - p3 - p7;

        for (int[] line : matrixC) {
            for (int element : line) {
                System.out.print(element + "\t");
            }
            System.out.print("\r\n");
        }
    }

    @Test
    public void pressureTest() {
        long begin = System.currentTimeMillis();
        int count = 10000;
        while (count > 0) {
            violent();
            count--;
        }
        long end = System.currentTimeMillis();

        long begin2 = System.currentTimeMillis();
        int count2 = 10000;
        while (count2 > 0) {
            simpleStrassen();
            count2--;
        }
        long end2 = System.currentTimeMillis();

        System.out.println("Violent:\tfinished in " + (end - begin) + " millisecond");
        System.out.println("Strassen:\tfinished in " + (end2 - begin2) + " millisecond");
    }

} 
