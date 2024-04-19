package com.example.arbexcelconverterweb.domain.excel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ARBWriter {

    String userPath = System.getProperty("user.home") + File.separator;
    private final Map<String, String> arbMap;

    public ARBWriter(Map<String, String> arbMap) {
        this.arbMap = arbMap;
    }

    List<File> arbFileWriter() {
        List<File> generatedFiles = new ArrayList<>();
        for (Map.Entry<String, String> languagesMap : arbMap.entrySet()) {
            String prefix = languagesMap.getKey(); // Use key as prefix
            String suffix = ".arb";
            File tempFile;
            try {
                tempFile = File.createTempFile(prefix, suffix);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            generatedFiles.add(tempFile);
        }
        return generatedFiles;
    }
}
