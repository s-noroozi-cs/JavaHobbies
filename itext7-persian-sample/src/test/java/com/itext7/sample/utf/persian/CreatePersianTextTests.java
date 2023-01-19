package com.itext7.sample.utf.persian;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

@Slf4j
public class CreatePersianTextTests {

    private void validate_result(String expected, String result){
        log.info("""
                 
                 ----------------------------
                 Expected: %s
                 Result  : %s
                 ----------------------------       
                        """.formatted(expected,result));
        Assertions.assertEquals(expected,result);
    }

    @Test
    void test_english_text(){
        String expectedText = "Thanks for any chances.";
        byte[] output = PdfUtil.createPdf(expectedText);
        String resultText = PdfUtil.readPdf(new ByteArrayInputStream(output));
        validate_result(expectedText,resultText);
    }

    @Test
    void test_persian_text()throws Exception{
        String expectedText = "برای هر شانسی متشکرم";
        byte[] output = PdfUtil.createPdf(expectedText);
        String resultText = PdfUtil.readPdf(new ByteArrayInputStream(output));
        validate_result(expectedText,resultText);
    }
}
