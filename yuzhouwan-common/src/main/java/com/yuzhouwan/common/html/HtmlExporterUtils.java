package com.yuzhouwan.common.html;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.yuzhouwan.common.util.PropUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: Html Exporter Utils
 *
 * @author Benedict Jin
 * @since 2016/6/20
 */
public final class HtmlExporterUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlExporterUtils.class);

    private HtmlExporterUtils() {
    }

    /**
     * 将 字符数组 转化为文件.
     *
     * @param bytes 文件的 byte[]
     * @param file  文件
     */
    public static void byte2File(byte[] bytes, File file) {
        try (OutputStream out = new FileOutputStream(file)) {
//            FileUtils.writeByteArrayToFile(file, bytes);  // same as next line
            out.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将图片转换为 A4 格式的 PDF.
     *
     * @param image 图片的 byte[]
     * @return A4 格式的 图片的 byte[]
     */
    public static byte[] image2pdfA4(byte[] image) {
        return image2pdf(image, null, null, null, null, null);
    }

    /**
     * 将图片转换为 PDF.
     *
     * @param image
     * @param pageSize     supported type will be found in com.itextpdf.text.PageSize,
     *                     like A2, A3, A4, LETTER_LANDSCAPE etc.
     * @param marginLeft   0f
     * @param marginRight  0f
     * @param marginTop    0f
     * @param marginBottom 0f
     * @return PDF 格式的 byte[]
     */
    public static byte[] image2pdf(byte[] image, Rectangle pageSize, Float marginLeft, Float marginRight,
                                   Float marginTop, Float marginBottom) {

        Document document = new Document(pageSize == null ? PageSize.A4 : pageSize,
                marginLeft == null ? 0f : marginLeft, marginRight == null ? 0f : marginRight,
                marginTop == null ? 0f : marginTop, marginBottom == null ? 0f : marginBottom);
        PdfWriter pdfWriter;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            pdfWriter = PdfWriter.getInstance(document, baos);
            document.open();
            document.add(Image.getInstance(image, true));
            // need close document and pdfWriter before convert byte array!
            document.close();
            pdfWriter.close();
            return baos.toByteArray();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化配置 PhantomJS Driver.
     *
     * @param url         目标 URL
     * @param addedCookie 添加 cookie
     * @return 初始化过的 PhantomJS Driver
     */
    public static PhantomJSDriver prepare(String url, Cookie addedCookie, Integer width, Integer height) {
        // chrome driver maybe not necessary
        // download from https://sites.google.com/a/chromium.org/chromedriver/downloads
//        System.setProperty("webdriver.chrome.driver",
//                DirUtils.RESOURCES_PATH.concat(
//                        PropUtils.getInstance().getProperty("html.exporter.webdriver.chrome.driver")));

        DesiredCapabilities phantomCaps = DesiredCapabilities.chrome();
        phantomCaps.setJavascriptEnabled(true);
        PropUtils p = PropUtils.getInstance();
        phantomCaps.setCapability("phantomjs.page.settings.userAgent",
                p.getProperty("html.exporter.user.agent"));

        PhantomJSDriver driver = new PhantomJSDriver(phantomCaps);
        driver.manage().timeouts().implicitlyWait(Integer.parseInt(
                p.getProperty("html.exporter.driver.timeouts.implicitly.seconds")), TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(Integer.parseInt(
                p.getProperty("html.exporter.driver.timeouts.page.load.seconds")), TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(Integer.parseInt(
                p.getProperty("html.exporter.driver.timeouts.script.seconds")), TimeUnit.SECONDS);

        if (width == null || height == null) driver.manage().window().maximize();
        else driver.manage().window().setSize(new Dimension(width, height));

        if (addedCookie != null) driver.manage().addCookie(addedCookie);

        driver.get(url);
//        try {
//            // timeout is not work, so fix it by sleeping thread
//            Thread.sleep(Integer.valueOf(PropUtils.getInstance()
//                    .getProperty("html.exporter.driver.timeouts.implicitly.seconds")) * 1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        return driver;
    }

    /**
     * 获取 [文件] 形式 的图片大小.
     *
     * @param image 图片文件
     * @return 图片大小
     */
    public static Rectangle getSizeFromImage(File image) {
        try {
            BufferedImage bufferedImage = ImageIO.read(image);
            return new Rectangle(bufferedImage.getWidth(), bufferedImage.getHeight());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取 [InputStream] 形式 的图片大小.
     *
     * @param inputStream 图片输入流
     * @return 图片大小
     */
    public static Rectangle getSizeFromImage(InputStream inputStream) {
        try {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            return new Rectangle(bufferedImage.getWidth(), bufferedImage.getHeight());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("Cannot close inputStream!", e);
                }
            }
        }
    }

    /**
     * 获取 [字符数组] 形式 的图片大小.
     *
     * @param bytes 图片的 byte[]
     * @return 图片大小
     */
    public static Rectangle getSizeFromImage(byte[] bytes) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            return new Rectangle(bufferedImage.getWidth(), bufferedImage.getHeight());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取 [Base64] 形式 的图片大小.
     *
     * @param base64 base64 格式的图片
     * @return 图片大小
     */
    public static Rectangle getSizeFromImage(String base64) {
        return getSizeFromImage(base64.getBytes());
    }

    /**
     * Release those resource of phantomjs, include shutdown phantom process.
     *
     * @param driver close cannot shutdown, should do it with quit()
     */
    public static void release(PhantomJSDriver driver) {
        try {
            if (driver != null) driver.close();
        } finally {
            if (driver != null) driver.quit();
        }
    }
}
