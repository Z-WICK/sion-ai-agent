package com.sion.sionaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PDFGenerationToolTest {

    @Test
    public void testGeneratePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "JohnSion.pdf";
        String content = "JohnSion blog -  https://blog.johncloud.us.kg";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}
