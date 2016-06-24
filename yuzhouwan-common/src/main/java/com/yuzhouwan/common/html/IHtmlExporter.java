package com.yuzhouwan.common.html;

import org.openqa.selenium.Cookie;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: com.yuzhouwan.common.html
 *
 * @author Benedict Jin
 * @since 2016/6/22
 */
public interface IHtmlExporter {

    /**
     * 将带有 chart、map等动态图表的 html转换为 图片
     *
     * @param url
     * @return
     */
    <OT> OT convert2Image(String url, Integer width, Integer height);

    /**
     * 将带有 chart、map等动态图表的 html转换为 图片 (可以额外配置 cookie的权限控制)
     *
     * @param url
     * @param addedCookie
     * @return
     */
    <OT> OT convert2Image(String url, Cookie addedCookie, Integer width, Integer height);

}
