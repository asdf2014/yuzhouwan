package com.yuzhouwan.common.html;

import com.yuzhouwan.common.util.PropUtils;
import com.yuzhouwan.common.util.StrUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: Html Exporter
 *
 * @author Benedict Jin
 * @since 2016/6/20
 */
public class HtmlExporter {

    /**
     * 将带有 chart、map等动态图表的 html转换为 图片
     *
     * @param url
     * @return
     */
    public static File convert2Image(String url) {
        return prepare(url, null).getScreenshotAs(OutputType.FILE);
    }

    /**
     * 将带有 chart、map等动态图表的 html转换为 图片 (可以额外配置 cookie的权限控制)
     *
     * @param url
     * @param addedCookie
     * @return
     */
    public static File convert2Image(String url, Cookie addedCookie) {
        return prepare(url, addedCookie).getScreenshotAs(OutputType.FILE);
    }

    /**
     * 初始化配置 PhantomJS Driver
     *
     * @param url
     * @param addedCookie
     * @return
     */
    private static PhantomJSDriver prepare(String url, Cookie addedCookie) {
        System.setProperty("webdriver.chrome.driver",
                PropUtils.getInstance().getProperty("html.exporter.webdriver.chrome.driver"));

        DesiredCapabilities phantomCaps = DesiredCapabilities.chrome();
        phantomCaps.setJavascriptEnabled(true);
        phantomCaps.setCapability("phantomjs.page.settings.userAgent",
                PropUtils.getInstance().getProperty("html.exporter.user.agent"));

        PhantomJSDriver driver = new PhantomJSDriver(phantomCaps);
        driver.manage().timeouts().implicitlyWait(Integer.valueOf(PropUtils.getInstance()
                .getProperty("html.exporter.driver.timeouts.implicitly.seconds")), TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(Integer.valueOf(PropUtils.getInstance()
                .getProperty("html.exporter.driver.timeouts.page.load.seconds")), TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(Integer.valueOf(PropUtils.getInstance()
                .getProperty("html.exporter.driver.timeouts.script.seconds")), TimeUnit.SECONDS);

        String widthStr = PropUtils.getInstance().getProperty("html.exporter.window.size.width");
        String heightStr = PropUtils.getInstance().getProperty("html.exporter.window.size.height");
        if (StrUtils.isEmpty(widthStr) || StrUtils.isEmpty(heightStr)) {
            driver.manage().window().maximize();
        } else {
            driver.manage().window().setSize(new Dimension(Integer.valueOf(widthStr), Integer.valueOf(heightStr)));
        }

        if (addedCookie != null)
            driver.manage().addCookie(addedCookie);

        driver.get(url);
        return driver;
    }

}
