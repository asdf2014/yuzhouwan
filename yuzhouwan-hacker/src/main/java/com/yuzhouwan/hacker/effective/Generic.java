package com.yuzhouwan.hacker.effective;

/**
 * Created by Benedict Jin on 2015/10/9.
 */
public class Generic<Y> {

    Y y;

    <T> T multiply(T a) {
        return a;
    }

    double multiply(double a, double b) {
        return a * b;
    }

    int multiply(int a, int b) {
        return a * b;
    }

    float multiply(float a, float b) {
        return a * b;
    }

    char multiply(char a, char b) {
        return (char) ((int) a * (int) b);
    }
}
