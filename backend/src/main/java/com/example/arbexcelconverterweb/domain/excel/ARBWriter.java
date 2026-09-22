package com.example.arbexcelconverterweb.domain.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ARBWriter {

    String userPath = System.getProperty("user.home") + File.separator;
    private final Map<String, String> arbMap;

    public ARBWriter(Map<String, String> arbMap) {
        this.arbMap = arbMap;
    }

    List<byte[]> arbFileWriter() {
        List<byte[]> generatedFiles = new ArrayList<>();
        for (Map.Entry<String, String> languagesMap : arbMap.entrySet()) {
            String content = languagesMap.getValue();
            generatedFiles.add(content.getBytes());
        }
        return generatedFiles;
    }
}
