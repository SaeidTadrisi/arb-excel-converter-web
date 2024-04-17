package com.example.arbexcelconverterweb.domain.excel;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExcelPlaceHolderExtractor {

    private final Map<String, String> eachLanguageMap;


    public ExcelPlaceHolderExtractor(Map<String, String> eachLanguageMap) {
        this.eachLanguageMap = eachLanguageMap;
    }

    public Map<String, String> placeHoldersExtractor(){
        Map<String, String> placeHoldersMap = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : eachLanguageMap.entrySet()){
            if (entry.getKey().startsWith("@")){
                placeHoldersMap.put(entry.getKey(), entry.getValue());
            }
        }
        return placeHoldersMap;
    }
}
