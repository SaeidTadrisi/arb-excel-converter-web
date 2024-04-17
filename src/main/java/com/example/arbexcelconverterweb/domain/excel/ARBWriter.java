package com.example.arbexcelconverterweb.domain.excel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class ARBWriter {

    String userPath = System.getProperty("user.home") + File.separator;
    private final Map<String, String> arbMap;

    public ARBWriter(Map<String, String> arbMap) {
        this.arbMap = arbMap;
    }

    void arbFileWriter() {
        for (Map.Entry<String, String> languagesMap : arbMap.entrySet()) {
            File outputFile = new File(userPath, languagesMap.getKey() + ".arb");
            try (FileWriter fileWriter = new FileWriter(outputFile)) {
                fileWriter.write(languagesMap.getValue());
            } catch (IOException e) {
                throw new IllegalArgumentException("No write permissions for the directory.");
            }
        }
    }

}
