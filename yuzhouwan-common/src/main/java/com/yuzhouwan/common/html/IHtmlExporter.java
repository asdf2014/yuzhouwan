package com.yuzhouwan.common.html;

import org.openqa.selenium.Cookie;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function: Html Exporter Interface
 * <p>
 * Should install the newest PhantomJS, and set into PATH.
 *
 * @author Benedict Jin
 * @see <a href="http://phantomjs.org/download.html">http://phantomjs.org/download.html</a>
 * @since 2016/6/22
 */
public interface IHtmlExporter<T> {

    /**
     * 将带有 chart、map 等动态图表的 html 转换为 图片.
     *
     * @param url 目标 URL
     * @return 未知类型的图片
     */
    T convert2Image(String url);

    /**
     * 将带有 chart、map 等动态图表的 html 转换为 图片.
     *
     * @param url 目标 URL
     * @return 未知类型的图片
     */
    T convert2Image(String url, Integer width, Integer height);

    /**
     * 将带有 chart、map 等动态图表的 html 转换为 图片（可以额外配置 cookie 的权限控制）.
     * 默认原始页面大小，作为图片的大小.
     *
     * @param url         目标 URL
     * @param addedCookie 添加 cookie
     * @return 未知类型的图片
     */
    T convert2Image(String url, Cookie addedCookie);


    /**
     * 将带有 chart、map 等动态图表的 html 转换为 图片（可以额外配置 cookie 的权限控制）.
     *
     * @param url         目标 URL
     * @param addedCookie 添加 cookie
     * @return 未知类型的图片
     */
    T convert2Image(String url, Cookie addedCookie, Integer width, Integer height);
}
