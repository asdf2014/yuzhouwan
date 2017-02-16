package com.yuzhouwan.common.html;

import org.openqa.selenium.Cookie;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: Html Exporter Interface
 * <p>
 * Should install the newest PhantomJS, and set into PATH.
 * <url>http://phantomjs.org/download.html</url>
 *
 * @author Benedict Jin
 * @since 2016/6/22
 */
public interface IHtmlExporter {

    /**
     * 将带有 chart、map等动态图表的 html转换为 图片
     *
     * @param url 目标URL
     * @return 未知类型的图片
     */
    <OT> OT convert2Image(String url);

    /**
     * 将带有 chart、map等动态图表的 html转换为 图片
     *
     * @param url 目标URL
     * @return 未知类型的图片
     */
    <OT> OT convert2Image(String url, Integer width, Integer height);

    /**
     * 将带有 chart、map等动态图表的 html转换为 图片 (可以额外配置 cookie的权限控制)
     * 默认原始页面大小，作为图片的大小
     *
     * @param url         目标URL
     * @param addedCookie 添加 cookie
     * @return 未知类型的图片
     */
    <OT> OT convert2Image(String url, Cookie addedCookie);


    /**
     * 将带有 chart、map等动态图表的 html转换为 图片 (可以额外配置 cookie的权限控制)
     *
     * @param url         目标URL
     * @param addedCookie 添加 cookie
     * @return 未知类型的图片
     */
    <OT> OT convert2Image(String url, Cookie addedCookie, Integer width, Integer height);

}
