package com.yuzhouwan.common.html;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: Html Exporter
 *
 * @author Benedict Jin
 * @since 2016/6/20
 */
public class HtmlExporter2BYTES implements IHtmlExporter {

    @Override
    public byte[] convert2Image(String url) {
        return convert2Image(url, null, null);
    }

    /**
     * 将带有 chart、map等动态图表的 html转换为 图片.
     *
     * @param url 目标URL
     * @return 图片 byte数组
     */
    @Override
    public byte[] convert2Image(String url, Integer width, Integer height) {
        return convert2Image(url, null, width, height);
    }

    @Override
    public byte[] convert2Image(String url, Cookie addedCookie) {
        return convert2Image(url, addedCookie, null, null);
    }

    /**
     * 将带有 chart、map等动态图表的 html转换为 图片 (可以额外配置 cookie的权限控制).
     *
     * @param url         目标URL
     * @param addedCookie 添加 cookie
     * @return 图片 byte数组
     */
    @SuppressWarnings(value = {"unchecked"})
    @Override
    public byte[] convert2Image(String url, Cookie addedCookie, Integer width, Integer height) {
        PhantomJSDriver driver = null;
        try {
            driver = HtmlExporterUtils.prepare(url, addedCookie, width, height);
            return driver == null ? null : driver.getScreenshotAs(OutputType.BYTES);
        } finally {
            HtmlExporterUtils.release(driver);
        }
    }
}
