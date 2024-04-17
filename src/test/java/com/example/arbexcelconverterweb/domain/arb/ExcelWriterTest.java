package com.example.arbexcelconverterweb.domain.arb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

class ExcelWriterTest {

    List<String> stringFiles;
    ARBReader arbReader;
    Map<String, String> simpleMap;
    Map<String, String> placeHolderMap;
    Map<String, String> combinedMap;

    @BeforeEach
    void setUp() {
        File file = new File("intl_en_test.arb");
        arbReader = new ARBReader(List.of(file), "intl_en_test.arb");
        stringFiles = arbReader.getARBStringFiles();

        SimpleElements simpleElements = new SimpleElements(stringFiles.getFirst());
        simpleMap = simpleElements.otherElementsExtractor();

        PlaceHolders placeHolders = new PlaceHolders(stringFiles.getFirst());
        placeHolderMap = placeHolders.placeHoldersExtractor();

        ElementsCombiner elementsCombiner = new ElementsCombiner(simpleMap, placeHolderMap);
        combinedMap = elementsCombiner.placeHolderTypeReplacer();
    }

    @Test
    void should_export_an_excel() {
        List<Map<String, String>> maps = List.of(combinedMap);
        ExcelWriter excelWriter = new ExcelWriter(maps);
        excelWriter.exportExcelFile();
    }

}