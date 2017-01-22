package com.yuzhouwan.hacker.algorithms.sort;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：BitMap4G Example
 *
 * @author Benedict Jin
 * @since 2016/8/2
 */
public class BitMap4GExample {

    private char[] bitmap;
    private int size;
    private int base;

    /**
     * 初始化 bitmap
     *
     * @param size  bitmap 的大小，即 bit 位的个数
     * @param start 起始值
     * @return 0：失败 1：成功
     */
    public int bitmapInit(int size, int start) {
        this.size = size / 8 + 1;
        base = start;
        bitmap = new char[this.size];

        if (bitmap == null)
            return 0;

        return 1;
    }

    /**
     * 将值 index 的对应位设为 1
     *
     * @param index 要设的值
     * @return 0：失败 1：成功
     */
    public int bitmapSet(int index) {
        int quo = (index - base) / 8; // 确定所在的字节
        int remainder = (index - base) % 8; // 字节内的偏移

        char x = (char) (0x1 << remainder);

        if (quo > size)
            return 0;

        bitmap[quo] |= x; // 所在字节内的特定位置为1

        return 1;
    }

    /**
     * 取 bitmap 第 i 位的值
     *
     * @param i 待取位
     * @return -1：失败，否则返回对应位的值
     */
    public int bitmapGet(int i) {
        int quo = (i) / 8;
        int remainder = (i) % 8;
        char x = (char) (0x1 << remainder);
        char res;

        if (quo > size)
            return -1;

        res = (char) (bitmap[quo] & x);

        return res > 0 ? 1 : 0;
    }

    /**
     * 返回 index 位对应的值
     *
     * @param index
     * @return
     */
    public int bitmapIndex(int index) {
        return index + base;
    }

    /**
     * 释放内存
     *
     * @return
     */
    public void bitmapFree() {
        bitmap = null;
    }
}
