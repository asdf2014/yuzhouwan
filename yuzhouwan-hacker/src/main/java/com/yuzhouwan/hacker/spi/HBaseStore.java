package com.yuzhouwan.hacker.spi;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function：HBase Store
 *
 * @author Benedict Jin
 * @see <a href="https://yuzhouwan.com/posts/45888/">Apache HBase 全攻略</a>
 * @since 2020/3/11
 */
public class HBaseStore implements IStore {

    @Override
    public void record(String data) {
        System.err.println("Recording " + data + " to HBase ...");
    }
}
