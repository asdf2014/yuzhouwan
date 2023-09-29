package com.yuzhouwan.hacker.spi;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Elasticsearch Store
 *
 * @author Benedict Jin
 * @see <a href="https://yuzhouwan.com/posts/22654/">搜索引擎 Elasticsearch</a>
 * @since 2020/3/11
 */
public class ESStore implements IStore {

    @Override
    public void record(String data) {
        System.err.println("Recording " + data + " to ES ...");
    }
}
