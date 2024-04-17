package com.example.arbexcelconverterweb.domain.arb;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ARBToExcelOrchestrator {

    private final List<File> arbFiles;
    private final String referenceFile;

    public ARBToExcelOrchestrator(List<File> arbFiles, String referenceFile) {
        this.arbFiles = arbFiles;
        this.referenceFile = referenceFile;
    }

    public void getExcelFile(){

        ARBReader arbReader = new ARBReader(arbFiles, referenceFile);
        List<String> arbStringFiles = arbReader.getARBStringFiles();

            List<Map<String, String>> mapList = new LinkedList<>();
            for (String arb : arbStringFiles) {
                Map<String, String> otherElementsMap = new ARBSimpleElementsExtractor(arb).otherElementsExtractor();
                Map<String, String> placeHoldersMap = new ARBPlaceHoldersExtractor(arb).placeHoldersExtractor();
                Map<String, String> combinedMap = new ARBElementsCombiner(otherElementsMap, placeHoldersMap).finalPatternedMap();
                mapList.add(combinedMap);
            }

        ExcelWriter excelWriter = new ExcelWriter(mapList);
            excelWriter.exportExcelFile();
    }
}
