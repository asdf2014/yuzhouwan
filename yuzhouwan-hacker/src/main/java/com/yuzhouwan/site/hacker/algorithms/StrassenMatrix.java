package com.yuzhouwan.site.hacker.algorithms;

import org.apache.commons.math3.exception.MathArithmeticException;

/**
 * Created by Benedict Jin on 2015/10/8.
 */
public class StrassenMatrix {

    public double[][] beforeStrassen(double[][] matrixA, double[][] matrixB) {

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
        double[][] matrixC = new double[rowSize][cloSize];


        return matrixC;
    }

    public double[][] strassen(double[][] matrixA, double[][] matrixB, double[][] matrixC, int rowLow, int rowHigh, int colLow, int colHigh) {

        int rowSizeA = matrixA.length;
        int colSizeA = matrixA[0].length;
        int rowSizeB = matrixB.length;
        int colSizeB = matrixB[0].length;

        int rowSize = rowHigh - rowLow;
        int colSize = colHigh - colLow;

        int rowMid = rowSize / 2;
        int colMid = colSize / 2;

        if ((rowHigh - rowLow) == 1 && (colHigh - colLow) == 1) {

            double[][] matrixResult = minMatrixMultiply(matrixA, matrixB, rowLow, rowHigh, colLow, colHigh, rowSize, colSize);
            matrixA[rowLow][colLow] = matrixResult[0][0];
            matrixA[rowLow][colHigh] = matrixResult[0][1];
            matrixA[rowHigh][colLow] = matrixResult[1][0];
            matrixA[rowHigh][colHigh] = matrixResult[1][1];
            return null;
        }

        strassen(matrixA, matrixB, matrixC, rowLow, rowMid, colLow, colMid);
        strassen(matrixA, matrixB, matrixC, rowMid + 1, rowHigh, colLow, colMid);
        strassen(matrixA, matrixB, matrixC, rowLow, rowMid, colMid + 1, colHigh);
        strassen(matrixA, matrixB, matrixC, rowMid + 1, rowHigh, colMid + 1, colHigh);

        return null;
    }

    /**
     * Add min sub-matrix [2 x 2].
     *
     * @param matrixA
     * @param matrixB
     * @return
     */
    public double[][] minMatrixAdd(double[][] matrixA, double[][] matrixB, int rowLow, int rowHigh, int colLow, int colHigh, int rowSize, int colSize) {

        int rowSizeA = matrixA.length;
        int colSizeA = matrixA[0].length;
        int rowSizeB = matrixB.length;
        int colSizeB = matrixB[0].length;

        boolean rowNeedExpand = false;
        if (rowSize % 2 != 0) {
            rowNeedExpand = true;
        }
        boolean colNeedExpand = false;
        if (colSize % 2 != 0) {
            colNeedExpand = true;
        }

        double a11 = rowLow < 0 || colLow < 0 ? 0 : matrixA[rowLow][colLow];
        double a12 = rowLow < 0 || colHigh > (colSizeA - 1) || colNeedExpand ? 0 : matrixA[rowLow][colHigh];
        double a21 = rowHigh > (rowSizeA - 1) || colLow < 0 || rowNeedExpand ? 0 : matrixA[rowHigh][colLow];
        double a22 = rowHigh > (rowSizeA - 1) || colHigh > (colSizeA - 1) || colNeedExpand || rowNeedExpand ? 0 : matrixA[rowHigh][colHigh];

        double b11 = rowLow < 0 || colLow < 0 ? 0 : matrixB[rowLow][colLow];
        double b12 = rowLow < 0 || colHigh > (colSizeB - 1) || colNeedExpand ? 0 : matrixB[rowLow][colHigh];
        double b21 = rowHigh > (rowSizeB - 1) || colLow < 0 || rowNeedExpand ? 0 : matrixB[rowHigh][colLow];
        double b22 = rowHigh > (rowSizeB - 1) || colHigh > (colSizeB - 1) || colNeedExpand || rowNeedExpand ? 0 : matrixB[rowHigh][colHigh];

        double[][] matrixC = notSparseMatrixFromAdd(rowHigh, colHigh, rowSizeA, colSizeA, a11, a12, a21, a22, b11, b12, b21, b22);
//        double[][] matrixC = sparseMatrixFromAdd(a11, a12, a21, a22, b11, b12, b21, b22);

        return matrixC;
    }

    private double[][] sparseMatrixFromAdd(double a11, double a12, double a21, double a22, double b11, double b12, double b21, double b22) {
        double[][] matrixC = new double[2][2];
        matrixC[0][0] = a11 + b11;
        matrixC[0][1] = a12 + b12;
        matrixC[1][0] = a21 + b21;
        matrixC[1][1] = a22 + b22;
        return matrixC;
    }

    private double[][] notSparseMatrixFromAdd(int rowHigh, int colHigh, int rowSizeA, int colSizeA, double a11, double a12, double a21, double a22, double b11, double b12, double b21, double b22) {
        int rowSizeC = 2;
        int colSizeC = 2;
        if (rowHigh > (rowSizeA - 1)) {
            rowSizeC = rowHigh - rowSizeA + 1;
        }
        if (colHigh > (colSizeA - 1)) {
            colSizeC = colHigh - colSizeA + 1;
        }
        double[][] matrixC = new double[rowSizeC][colSizeC];
        for (int i = 0; i < rowSizeC; i++) {
            for (int j = 0; j < colSizeC; j++) {
                if (i == 0 && j == 0)
                    matrixC[0][0] = a11 + b11;
                if (i == 0 && j == 1)
                    matrixC[0][1] = a12 + b12;
                if (i == 1 && j == 0)
                    matrixC[1][0] = a21 + b21;
                if (i == 1 && j == 1)
                    matrixC[1][1] = a22 + b22;
            }
        }
        return matrixC;
    }

    /**
     * Multiply min sub-matrix [2 x 2].
     *
     * @param matrixA
     * @param matrixB
     * @param rowLow
     * @param rowHigh
     * @param colLow
     * @param colHigh
     * @return
     */
    public double[][] minMatrixMultiply(double[][] matrixA, double[][] matrixB, int rowLow, int rowHigh, int colLow, int colHigh, int rowSize, int colSize) {

        boolean rowNeedExpand = false;
        if (rowSize % 2 != 0) {
            rowNeedExpand = true;
        }
        boolean colNeedExpand = false;
        if (colSize % 2 != 0) {
            colNeedExpand = true;
        }

        int rowSizeA = matrixA.length;
        int colSizeA = matrixA[0].length;
        int rowSizeB = matrixB.length;
        int colSizeB = matrixB[0].length;

        double a11 = rowLow < 0 || colLow < 0 ? 0 : matrixA[rowLow][colLow];
        double a12 = rowLow < 0 || colHigh > (colSizeA - 1) || colNeedExpand ? 0 : matrixA[rowLow][colHigh];
        double a21 = rowHigh > (rowSizeA - 1) || colLow < 0 || rowNeedExpand ? 0 : matrixA[rowHigh][colLow];
        double a22 = rowHigh > (rowSizeA - 1) || colHigh > (colSizeA - 1) || colNeedExpand || rowNeedExpand ? 0 : matrixA[rowHigh][colHigh];

        double b11 = rowLow < 0 || colLow < 0 ? 0 : matrixB[rowLow][colLow];
        double b12 = rowLow < 0 || colHigh > (colSizeB - 1) || colNeedExpand ? 0 : matrixB[rowLow][colHigh];
        double b21 = rowHigh > (rowSizeB - 1) || colLow < 0 || rowNeedExpand ? 0 : matrixB[rowHigh][colLow];
        double b22 = rowHigh > (rowSizeB - 1) || colHigh > (colSizeB - 1) || colNeedExpand || rowNeedExpand ? 0 : matrixB[rowHigh][colHigh];

        double s1 = b12 - b22;
        double s2 = a11 + a12;
        double s3 = a21 + a22;
        double s4 = b21 - b11;
        double s5 = a11 + a22;
        double s6 = b11 + b22;
        double s7 = a12 - a22;
        double s8 = b21 + b22;
        double s9 = a11 - a21;
        double s10 = b11 + b12;

        double p1 = a11 * s1;
        double p2 = s2 * b22;
        double p3 = s3 * b11;
        double p4 = a22 * s4;
        double p5 = s5 * s6;
        double p6 = s7 * s8;
        double p7 = s9 * s10;

        double[][] matrixC = notSparseMatrixFromMultipy(rowHigh, colHigh, rowSizeA, colSizeA, p1, p2, p3, p4, p5, p6, p7);
//        double[][] matrixC = sparseMatrixFromMultipy(p1, p2, p3, p4, p5, p6, p7);

        return matrixC;
    }

    private double[][] sparseMatrixFromMultipy(double p1, double p2, double p3, double p4, double p5, double p6, double p7) {
        double[][] matrixC = new double[2][2];
        matrixC[0][0] = p5 + p4 - p2 + p6;
        matrixC[0][1] = p1 + p2;
        matrixC[1][0] = p3 + p4;
        matrixC[1][1] = p5 + p1 - p3 - p7;
        return matrixC;
    }

    /**
     * Get the simplest matrix [<2 x <2].
     *
     * @param rowHigh
     * @param colHigh
     * @param rowSizeA
     * @param colSizeA
     * @param p1
     * @param p2
     * @param p3
     * @param p4
     * @param p5
     * @param p6
     * @param p7
     * @return
     */
    private double[][] notSparseMatrixFromMultipy(int rowHigh, int colHigh, int rowSizeA, int colSizeA, double p1, double p2, double p3, double p4, double p5, double p6, double p7) {
        int rowSizeC = 2;
        int colSizeC = 2;

        if (rowHigh > (rowSizeA - 1)) {
            rowSizeC = rowHigh - rowSizeA + 1;
        }
        if (colHigh > (colSizeA - 1)) {
            colSizeC = colHigh - colSizeA + 1;
        }
        double[][] matrixC = new double[rowSizeC][colSizeC];
        for (int i = 0; i < rowSizeC; i++) {
            for (int j = 0; j < colSizeC; j++) {
                if (i == 0 && j == 0)
                    matrixC[0][0] = p5 + p4 - p2 + p6;
                if (i == 0 && j == 1)
                    matrixC[0][1] = p1 + p2;
                if (i == 1 && j == 0)
                    matrixC[1][0] = p3 + p4;
                if (i == 1 && j == 1)
                    matrixC[1][1] = p5 + p1 - p3 - p7;
            }
        }
        return matrixC;
    }

}