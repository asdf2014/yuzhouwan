package com.yuzhouwan.common.html;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: Html Exporter
 *
 * @author Benedict Jin
 * @since 2016/6/20
 */
public class HtmlExporter2BASE64 implements IHtmlExporter<String> {

    @Override
    public String convert2Image(String url) {
        return convert2Image(url, null, null);
    }

    /**
     * 将带有 chart、map 等动态图表的 html 转换为 图片.
     *
     * @param url 目标URL
     */
    @Override
    public String convert2Image(String url, Integer width, Integer height) {
        return convert2Image(url, null, width, height);
    }

    @Override
    public String convert2Image(String url, Cookie addedCookie) {
        return convert2Image(url, addedCookie, null, null);
    }

    /**
     * 将带有 chart、map 等动态图表的 html 转换为 图片（可以额外配置 cookie 的权限控制）.
     *
     * @param url         目标 URL
     * @param addedCookie 添加 cookie
     * @return 图片 string 字符串
     */
    @Override
    public String convert2Image(String url, Cookie addedCookie, Integer width, Integer height) {
        PhantomJSDriver driver = null;
        try {
            driver = HtmlExporterUtils.prepare(url, addedCookie, width, height);
            return driver == null ? "" : driver.getScreenshotAs(OutputType.BASE64);
        } finally {
            HtmlExporterUtils.release(driver);
        }
    }
}
