package com.yuzhouwan.hacker.effective;

import org.junit.Test;

/**
 * Created by Benedict Jin on 2015/10/9.
 */
public class Generic<Y> {

    Y y;

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

    @Test
    public void originTest() {
        System.out.println(multiply(1, 1));
        System.out.println(multiply(1d, 1d));
        System.out.println(multiply(1f, 1f));
        System.out.println(Math.sqrt((int) multiply('1', '1')));
    }

    <T> T multiply(T a) {
        return a;
    }

    @Test
    public void genericTest() {

        System.out.println(multiply(1).getClass());
        System.out.println(multiply(2f).getClass());
        System.out.println(multiply(3d).getClass());
    }

    @Test
    public void classGenericTest(){

        Generic<String> g = new Generic<>();
        g.y = "1";
    }

}
