package com.yuzhouwan.common.html;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: Html Exporter
 *
 * @author Benedict Jin
 * @since 2016/6/20
 */
public class HtmlExporter2BYTES implements IHtmlExporter {

    /**
     * 将带有 chart、map等动态图表的 html转换为 图片
     *
     * @param url
     * @return
     */
    @Override
    public byte[] convert2Image(String url, Integer width, Integer height) {
        return convert2Image(url, null, width, height);
    }

    /**
     * 将带有 chart、map等动态图表的 html转换为 图片 (可以额外配置 cookie的权限控制)
     *
     * @param url
     * @param addedCookie
     * @return
     */
    @SuppressWarnings(value = {"unchecked"})
    @Override
    public byte[] convert2Image(String url, Cookie addedCookie, Integer width, Integer height) {
        return HtmlExporterUtils.prepare(url, addedCookie, width, height).getScreenshotAs(OutputType.BYTES);
    }

}
