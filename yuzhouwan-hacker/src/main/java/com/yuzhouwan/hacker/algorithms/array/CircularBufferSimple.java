package com.yuzhouwan.hacker.algorithms.array;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Simple Circular Buffer
 *
 * @author Benedict Jin
 * @since 2017/02/28
 */
public class CircularBufferSimple {

    private int len, index;
    private int[] buffer;

    public CircularBufferSimple(int len) {
        this.len = len;
        this.buffer = new int[len];
    }

    public void buffer(int input) {
        index++;
        buffer[index % len] = input;
    }

    public int[] getBuffer() {
        return buffer;
    }
}
