package com.yuzhouwan.common.html;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.Cookie;

import java.io.File;
import java.util.Date;

import static com.yuzhouwan.common.util.DirUtils.RESOURCES_PATH;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: Html Exporter Tester
 *
 * @author Benedict Jin
 * @since 2016/6/20
 */
public class HtmlExporterTest {

    private static final String PREFIX_OUTPUT_PATH = "html/";

    @Test
    public void simpleTest() throws Exception {
        String url = "http://portal.cloudguarder.com:81";
        FileUtils.copyFile(HtmlExporter.convert2Image(url),
                new File(RESOURCES_PATH.concat(PREFIX_OUTPUT_PATH).concat("login.png")));
    }

    /**
     * 如果 需要在 web工程中使用 phantomJS 的 render功能，可以尝试用 redis鉴权 替代cookie
     *
     * @throws Exception
     */
    @Test
    public void convert2ImageTest() throws Exception {

        String name = "TOKEN_PORTAL";
        String value = "15_3e5acc70c5ea5298bca1dc6e6edc52e39f1699341fc06df52da9c2c920470b55da42f22b0652f8d34c6c007814390ffc27226459c169c072e2262f35d3258780";
        // 这里需要特定地在 domain之前，加上一个 "."
        String domain = ".portal.cloudguarder.com";
        String path = "/";
        Date expiry = new Date(System.currentTimeMillis() + 1000L * 60L * 60L);
        boolean isSecure = false;
        boolean isHttpOnly = false;
        Cookie cookie = new Cookie(name, value, domain, path, expiry, isSecure, isHttpOnly);

        String url = "http://portal.cloudguarder.com:81/#/index/defend_monitor";
        FileUtils.copyFile(HtmlExporter.convert2Image(url, cookie),
                new File(RESOURCES_PATH.concat(PREFIX_OUTPUT_PATH).concat("month_report.png")));
    }
}
