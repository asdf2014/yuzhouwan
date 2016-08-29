package com.yuzhouwan.common.html;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Html Exporter
 *
 * @author Benedict Jin
 * @since 2016/6/20
 */
public class HtmlExporter2BASE64 implements IHtmlExporter {

    /**
     * 将带有 chart、map等动态图表的 html转换为 图片
     *
     * @param url 目标URL
     * @return
     */
    @SuppressWarnings(value = {"unchecked"})
    @Override
    public String convert2Image(String url, Integer width, Integer height) {
        return convert2Image(url, null, width, height);
    }

    /**
     * 将带有 chart、map等动态图表的 html转换为 图片 (可以额外配置 cookie的权限控制)
     *
     * @param url         目标URL
     * @param addedCookie 添加 cookie
     * @return
     */
    @SuppressWarnings(value = {"unchecked"})
    @Override
    public String convert2Image(String url, Cookie addedCookie, Integer width, Integer height) {
        PhantomJSDriver driver = null;
        try {
            driver = HtmlExporterUtils.prepare(url, addedCookie, width, height);
            if (driver == null)
                return "";
            return driver.getScreenshotAs(OutputType.BASE64);
        } finally {
            HtmlExporterUtils.release(driver);
        }
    }

}
