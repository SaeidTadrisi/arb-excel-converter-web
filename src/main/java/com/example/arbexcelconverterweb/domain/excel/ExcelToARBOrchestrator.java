package com.example.arbexcelconverterweb.domain.excel;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExcelToARBOrchestrator {

    private final File excelFile;

    public ExcelToARBOrchestrator(File excelFile) {
        this.excelFile = excelFile;
    }

    public void getArbFiles(){
        ExcelReader excelReader = new ExcelReader(excelFile);
        Map<String, Map<String, String>> excelStringFile = excelReader.getExcelStringFile();
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
        arbWriter.arbFileWriter();
    }
}
