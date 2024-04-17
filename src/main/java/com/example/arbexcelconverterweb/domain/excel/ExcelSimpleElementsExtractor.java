package com.example.arbexcelconverterweb.domain.excel;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExcelSimpleElementsExtractor {

    private final Map<String, String> eachLanguageMap;


    public ExcelSimpleElementsExtractor(Map<String, String> eachLanguageMap) {
        this.eachLanguageMap = eachLanguageMap;
    }

    Map<String, String> otherElementsExtractor(){
        Map<String, String> otherElementsMap = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : eachLanguageMap.entrySet()){
            if (!entry.getKey().startsWith("@")){
                otherElementsMap.put(entry.getKey(), entry.getValue());
            }
        }
        return otherElementsMap;
    }
}
