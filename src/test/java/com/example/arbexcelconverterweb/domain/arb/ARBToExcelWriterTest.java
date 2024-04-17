package com.example.arbexcelconverterweb.domain.arb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

class ARBToExcelWriterTest {

    List<String> stringFiles;
    ARBReader ARBReader;
    Map<String, String> simpleMap;
    Map<String, String> placeHolderMap;
    Map<String, String> combinedMap;

    @BeforeEach
    void setUp() {
        File file = new File("intl_en_test.arb");
        ARBReader = new ARBReader(List.of(file), "intl_en_test.arb");
        stringFiles = ARBReader.getARBStringFiles();

        ARBSimpleElementsExtractor ARBSimpleElementsExtractor = new ARBSimpleElementsExtractor(stringFiles.getFirst());
        simpleMap = ARBSimpleElementsExtractor.otherElementsExtractor();

        ARBPlaceHoldersExtractor ARBPlaceHoldersExtractor = new ARBPlaceHoldersExtractor(stringFiles.getFirst());
        placeHolderMap = ARBPlaceHoldersExtractor.placeHoldersExtractor();

        ARBElementsCombiner ARBElementsCombiner = new ARBElementsCombiner(simpleMap, placeHolderMap);
        combinedMap = ARBElementsCombiner.placeHolderTypeReplacer();
    }

    @Test
    void should_export_an_excel() {
        List<Map<String, String>> maps = List.of(combinedMap);
        ARBToExcelWriter ARBToExcelWriter = new ARBToExcelWriter(maps);
        ARBToExcelWriter.exportExcelFile();
    }

}