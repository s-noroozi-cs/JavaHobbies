package com.itext7.sample.utf.persian;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.SimpleTextExtractionStrategy;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.BaseDirection;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static com.itextpdf.kernel.pdf.PdfName.*;

public class PdfUtil {

    public static String readPdf(InputStream pdfContent){
        try {
            PdfReader pdfReader = new PdfReader(pdfContent);
            PdfDocument pdfDoc = new PdfDocument(pdfReader);
            ITextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
            return PdfTextExtractor.getTextFromPage(pdfDoc.getPage(1),strategy);
        }catch (Throwable ex){
            throw new RuntimeException(ex.getMessage(),ex);
        }
    }

    public static byte[] createPdf(String content) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            //Initialize PDF writer
            PdfWriter writer = new PdfWriter(baos);

            //Initialize PDF document
            PdfDocument pdf = new PdfDocument(writer);

            PdfFont pdfFont = PdfFontFactory.createFont("TAHOMA_0.TTF"
                    , PdfEncodings.IDENTITY_H
                    ,PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);

//            PdfFont font = PdfFontFactory.createFont("", PdfEncodings.IDENTITY_H, true);


            // Initialize document
            Document document = new Document(pdf);


            //Add paragraph to the document
            Paragraph paragraph = new Paragraph(content);
            paragraph.setFont(pdfFont);
            paragraph.setFontScript(Character.UnicodeScript.ARABIC);
            paragraph.setBaseDirection(BaseDirection.RIGHT_TO_LEFT);


            document.add(paragraph);

            //Close document
            document.close();

            return baos.toByteArray();

        }catch (Throwable ex){
            throw new RuntimeException(ex.getMessage(),ex);
        }
    }
}
