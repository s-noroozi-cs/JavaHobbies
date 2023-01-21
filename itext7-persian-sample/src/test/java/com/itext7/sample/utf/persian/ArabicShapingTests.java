package com.itext7.sample.utf.persian;

import com.ibm.icu.text.ArabicShaping;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

public class ArabicShapingTests {

    @Test
    void test_arabic_text() throws Exception {
        String salam = "سلام";
        String shaped = new StringBuilder(new ArabicShaping(
                ArabicShaping.LETTERS_SHAPE).shape(salam))
                .toString();

        System.out.println(salam);
        System.out.println(shaped);
        System.out.println("---------------");

        System.out.println(HexFormat.of().formatHex(salam.getBytes(StandardCharsets.UTF_8)));
        System.out.println(HexFormat.of().formatHex(shaped.getBytes(StandardCharsets.UTF_8)));
        System.out.println("---------------");

        System.out.println(toUnicode(salam));
        System.out.println(toUnicode(shaped));
        System.out.println("---------------");


    }

    private String toUnicode(String text){
        StringBuilder sb = new StringBuilder();


//        for(char ch : text.toCharArray()) {
//         //   sb.append("\\u" + Integer.toHexString(ch | 0x10000).substring(1));
//            sb.append(String.format ("\\u%04x", (int)ch));
//        }
//        return sb.toString();

            for (int index = 0; index < text.length(); index++) {

                // Convert the integer to a hexadecimal code.
                String hexCode = Integer.toHexString(text.codePointAt(index)).toUpperCase();


                // but the it must be a four number value.
                String hexCodeWithAllLeadingZeros = "0000" + hexCode;
                String hexCodeWithLeadingZeros = hexCodeWithAllLeadingZeros.substring(hexCodeWithAllLeadingZeros.length() - 4);

                sb.append("\\u" + hexCodeWithLeadingZeros);
                sb.append(" ");
                sb.append(text.charAt(index));
                sb.append("\n");
            }
        return sb.toString();

    }
}
