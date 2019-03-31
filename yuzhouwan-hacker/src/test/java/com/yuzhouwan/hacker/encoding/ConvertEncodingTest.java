package com.yuzhouwan.hacker.encoding;

import com.yuzhouwan.hacker.effective.encoding.ConvertEncoding;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * Copyright @ yuzhouwan.com
 * All right reserved.
 * Function：ConvertEncoding Tester
 *
 * @author Benedict Jin
 * @since 2015/11/19
 */
public class ConvertEncodingTest {

    private static final String utf8 = new String("阿斯多夫".getBytes(), StandardCharsets.UTF_8);
    private ConvertEncoding convertEncoding;

    @Before
    public void before() {
        convertEncoding = new ConvertEncoding();
    }

    @Before
    public void after() {

    }

    /**
     * Method: showUtf8InGB2312(String utf8)
     */
    @Test
    public void testShowUtf8InGB2312() {

        convertEncoding.showUtf8InGB2312(utf8);
    }

    /**
     * Method: showUtf8InGBK(String utf8)
     */
    @Test
    public void testShowUtf8InGBK() {

        convertEncoding.showUtf8InGBK(utf8);
    }

} 
