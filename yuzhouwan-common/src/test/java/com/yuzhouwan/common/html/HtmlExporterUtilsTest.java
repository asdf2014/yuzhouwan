package com.yuzhouwan.common.html;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Cookie;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Date;

import static com.yuzhouwan.common.dir.DirUtils.RESOURCES_PATH;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Html Exporter Tester
 *
 * @author Benedict Jin
 * @since 2016/6/20
 */
public class HtmlExporterUtilsTest {

    private static final String PREFIX_OUTPUT_PATH = "html/";

    private static final String name = "TOKEN_PORTAL";
    private static final String value = "15_1248679b373b5cbc8cc3c4a0a332244938aaa1c087d547243bfd190d544acc85cba0477e7e9bf8b35409c94f50dde673b1eb91055f192d236b02dd3ad2e8e25b";
    private static final String domain = ".portal.cloudguarder.com";
    private static final String path = "/";
    private static final Date expiry = new Date(System.currentTimeMillis() + 1000L * 60L * 60L);
    private static final boolean isSecure = false;
    private static final boolean isHttpOnly = false;

    private static final String url =
            //"http://192.168.112.189:8100/#/report?instanceId=22&customerId=5";
            "http://portal.cloudguarder.com:81/#/index/defend_monitor";

    //    @Test
    public void simpleTest() throws Exception {
        // 7s 306
        String url = "http://portal.cloudguarder.com";
        FileUtils.copyFile(new HtmlExporter2File().convert2Image(url),
                new File(RESOURCES_PATH.concat(PREFIX_OUTPUT_PATH).concat("login.png")));
    }

    //    @Test
    public void testTimeout() throws Exception {
        // 38s 451ms
        String url = "http://echarts.baidu.com/echarts2/doc/doc.html";
        FileUtils.copyFile(new HtmlExporter2File().convert2Image(url),
                new File(RESOURCES_PATH.concat(PREFIX_OUTPUT_PATH).concat("echarts.png")));
    }

    /**
     * 如果 需要在 web工程中使用 phantomJS 的 render功能，可以尝试用 redis鉴权 替代cookie
     *
     * @throws Exception
     */
    //    @Test
    public void convert2ImageTest() throws Exception {

        Cookie cookie = new Cookie(name, value, domain, path, expiry, isSecure, isHttpOnly);

        File image = new HtmlExporter2File().convert2Image(url, cookie);
        if (image != null) {
            FileUtils.copyFile(image, new File(RESOURCES_PATH.concat(PREFIX_OUTPUT_PATH).concat("month_report.png")));
        }
    }

    //    @Test
    public void image2pdfTest() throws Exception {
        String url = "http://portal.cloudguarder.com:81";
        HtmlExporter2BYTES htmlExporter2BYTES = new HtmlExporter2BYTES();
        byte[] bytes = htmlExporter2BYTES.convert2Image(url, null, null);
        HtmlExporterUtils.byte2File(HtmlExporterUtils.image2pdf(
                bytes,
                HtmlExporterUtils.getSizeFromImage(new ByteArrayInputStream(bytes)),
                0f, 0f, 0f, 0f),
                new File(RESOURCES_PATH.concat(PREFIX_OUTPUT_PATH).concat("login.pdf")));
    }

    //    @Test
    public void convert2PdfTest() throws Exception {

        Cookie cookie = new Cookie(name, value, domain, path, expiry, isSecure, isHttpOnly);

        HtmlExporter2BYTES htmlExporter2BYTES = new HtmlExporter2BYTES();
        byte[] bytes = htmlExporter2BYTES.convert2Image(url, cookie, 1366, /*6474*/null);

        HtmlExporterUtils.byte2File(HtmlExporterUtils.image2pdf(
                bytes,
                HtmlExporterUtils.getSizeFromImage(new ByteArrayInputStream(bytes)),
                0f, 0f, 0f, 0f),
                new File(RESOURCES_PATH.concat(PREFIX_OUTPUT_PATH).concat("month_report.pdf")));
    }
}
