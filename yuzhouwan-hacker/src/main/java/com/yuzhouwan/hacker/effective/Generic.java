package com.yuzhouwan.hacker.effective;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Generic
 *
 * @param <Y>
 * @author Benedict Jin
 * @since 2015/10/9
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
