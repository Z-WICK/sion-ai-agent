package com.sion.sionaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileOperationToolTest {

    @Test
    public void testReadFile() {

        FileOperationTool fileOperationTool = new FileOperationTool();
        String fileName = "JohnSion.txt";
        String result = fileOperationTool.readFile(fileName);
        assertNotNull(result);

    }

    @Test
    void testWriteFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String fileName = "JohnSion.txt";
        String content = "Hello, World!";
        String result = fileOperationTool.writeFile(fileName, content);
        assertNotNull(result);
    }
}