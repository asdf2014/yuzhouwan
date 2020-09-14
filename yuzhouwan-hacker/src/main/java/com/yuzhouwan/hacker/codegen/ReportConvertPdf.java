package com.yuzhouwan.hacker.codegen;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Copyright @ 2020 yuzhouwan.com
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


        PDFont font = PDType1Font.HELVETICA;
        float fontSize = 26;
        float x = 220, y = 750;
        String text = "yuzhouwan.com";

        drawString(content, font, fontSize, x, y, text);
        drawString(content, PDType1Font.HELVETICA, 16, 80, 700, "Real-time ML with Spark");


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
        return new PDPageContentStream(doc, page, true, true, true);
    }

    private static void finish(String pdfPath, PDDocument doc, PDPageContentStream content)
            throws IOException, COSVisitorException {
        content.close();
        doc.save(pdfPath);
        doc.close();
    }

    private static PDPage getPageByIndex(PDDocument doc, int pageIndex) {
        return (PDPage) doc.getDocumentCatalog().getAllPages().get(pageIndex);
    }

    private static void drawImageWithScale(PDDocument doc, PDPageContentStream content, String imgPath,
                                           int bufferedImageType, float scale) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(imgPath));
        BufferedImage image = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImageType);
        image.createGraphics().drawRenderedImage(bufferedImage, null);

        PDXObjectImage xImage = new PDPixelMap(doc, image);

        content.drawXObject(xImage, 100, 100, xImage.getWidth() * scale, xImage.getHeight() * scale);
    }

    private static void drawString(PDPageContentStream content, PDFont font, float fontSize, float x,
                                   float y, String text) throws IOException {
        content.beginText();
        content.setFont(font, fontSize);
        content.moveTextPositionByAmount(x, y);
        content.drawString(text);
        content.endText();
    }
}
