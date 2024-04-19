package com.example.arbexcelconverterweb.domain.arb;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ARBToExcelOrchestrator {

    private final List<String> arbStringFiles;

    public ARBToExcelOrchestrator(List<String> arbStringFiles) {
        this.arbStringFiles = arbStringFiles;
    }

    public void getExcelFile(){

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
