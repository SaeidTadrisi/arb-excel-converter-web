package com.example.arbexcelconverterweb.domain.arb;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleElements {

    private final String stringFile;

    public SimpleElements(String stringFile) {
        this.stringFile = stringFile;

    }

    public Map<String, String> otherElementsExtractor() {
        Map<String, String> stringMap = new LinkedHashMap<>();
        String[] lines = stringFile.split("\n");

        for (String line : lines) {
            if (line.contains("\": \"")) {
                String[] parts = line.split("\": \"");
                if (parts[0].startsWith("  \"")) {
                    if (parts[0].contains("@@")) {
                        languageKeyFinder(stringMap, parts);
                    } else {
                        keyValuesElementsFinder(stringMap, parts);
                    }
                }
            }
        }
        return stringMap;
    }

    private void languageKeyFinder(Map<String, String> stringMap, String[] parts) {
        String key = "Key";
        String value = parts[1].trim().replace("\",", "").replace("\"", "");
        stringMap.put(key, value);
    }

    private void keyValuesElementsFinder(Map<String, String> stringMap, String[] parts) {
        String key = parts[0].trim().replace("\"", "");
        String value = parts[1].trim().replace("\",", "").replace("\"", "");
        stringMap.put(key, value);
    }
}
