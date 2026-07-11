package com.yuzhouwan.hacker.codegen;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Code Generator
 *
 * @author Benedict Jin
 * @since 2016/1/26 0026
 */
public class ReportConvertPdf {

    public static void main(String[] args) throws Exception {

        String pdfPath = "C:\\Users\\asdf2014\\Desktop\\box.pdf";

        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        PDPageContentStream content = createContent(doc, page);


        PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        float fontSize = 26;
        float x = 220, y = 750;
        String text = "yuzhouwan.com";

        drawString(content, font, fontSize, x, y, text);
        drawString(content, new PDType1Font(Standard14Fonts.FontName.HELVETICA), 16, 80, 700, "Real-time ML with Spark");


        String imgPath = "C:\\Users\\asdf2014\\Desktop\\Y.jpg";
        int bufferedImageType = BufferedImage.TYPE_4BYTE_ABGR;
        float scale = 0.3f;

        drawImageWithScale(doc, content, imgPath, bufferedImageType, scale);


        int pageIndex = 0;
        PDPage page0 = getPageByIndex(doc, pageIndex);

        finish(pdfPath, doc, content);
    }

    private static PDPageContentStream createContent(PDDocument doc, PDPage page) throws IOException {
        doc.addPage(page);
        return new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
    }

    private static void finish(String pdfPath, PDDocument doc, PDPageContentStream content)
            throws IOException {
        content.close();
        doc.save(pdfPath);
        doc.close();
    }

    private static PDPage getPageByIndex(PDDocument doc, int pageIndex) {
        return doc.getPage(pageIndex);
    }

    private static void drawImageWithScale(PDDocument doc, PDPageContentStream content, String imgPath,
                                           int bufferedImageType, float scale) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(imgPath));
        BufferedImage image = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImageType);
        image.createGraphics().drawRenderedImage(bufferedImage, null);

        PDImageXObject xImage = LosslessFactory.createFromImage(doc, image);

        content.drawImage(xImage, 100, 100, xImage.getWidth() * scale, xImage.getHeight() * scale);
    }

    private static void drawString(PDPageContentStream content, PDFont font, float fontSize, float x,
                                   float y, String text) throws IOException {
        content.beginText();
        content.setFont(font, fontSize);
        content.newLineAtOffset(x, y);
        content.showText(text);
        content.endText();
    }
}
