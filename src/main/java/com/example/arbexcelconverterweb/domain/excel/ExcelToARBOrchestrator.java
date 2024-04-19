package com.example.arbexcelconverterweb.domain.excel;

import com.example.arbexcelconverterweb.application.ExcelReaderImpl;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelToARBOrchestrator {

    private final Map<String, Map<String, String>> excelStringFile;


    public ExcelToARBOrchestrator(Map<String, Map<String, String>> excelStringFile) {
        this.excelStringFile = excelStringFile;
    }

    public List<File> getArbFiles(){
        LinkedHashMap<String, String> collect = excelStringFile.entrySet().stream()
                .collect(LinkedHashMap::new,
                        (stringMap, languagesMapEntry) -> {
                            Map<String, String> eachLanguagesMap = new LinkedHashMap<>(languagesMapEntry.getValue());
                            Map<String, String> otherElementsMap =
                                    new ExcelSimpleElementsExtractor(eachLanguagesMap).otherElementsExtractor();
                            Map<String, String> placeHolderMap =
                                    new ExcelPlaceHolderExtractor(eachLanguagesMap).placeHoldersExtractor();
                            String jsonString = new ExcelElementsCombiner(otherElementsMap, placeHolderMap).arbPatternedMap();
                            stringMap.put(languagesMapEntry.getKey(), jsonString);
                        },
                        LinkedHashMap::putAll);
        ARBWriter arbWriter = new ARBWriter(collect);
        return arbWriter.arbFileWriter();
    }
}
