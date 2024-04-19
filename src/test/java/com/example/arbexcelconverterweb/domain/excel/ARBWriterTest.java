package com.example.arbexcelconverterweb.domain.excel;

import com.example.arbexcelconverterweb.application.ExcelReaderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

class ARBWriterTest {

    Map<String, String> simpleMap;
    Map<String, String> placeHolderMap;
    String arbPatternedString;


    @BeforeEach
    void setUp() {
        File file = new File("en.xlsx");
        ExcelReaderImpl excelReaderImpl = new ExcelReaderImpl(file);
        Map<String, Map<String, String>> excelStringFile = excelReaderImpl.getExcelStringFile();

        ExcelPlaceHolderExtractor excelPlaceHolderExtractor = new ExcelPlaceHolderExtractor(excelStringFile.get("en"));
        placeHolderMap = excelPlaceHolderExtractor.placeHoldersExtractor();

        ExcelSimpleElementsExtractor excelSimpleElementsExtractor =
                new ExcelSimpleElementsExtractor(excelStringFile.get("en"));
        simpleMap = excelSimpleElementsExtractor.otherElementsExtractor();

        ExcelElementsCombiner excelElementsCombiner = new ExcelElementsCombiner(simpleMap, placeHolderMap);
        arbPatternedString = excelElementsCombiner.arbPatternedMap();


    }

    @Test
    void should_export_an_arb() {
        Map<String,String> arbMap = Map.of("es", arbPatternedString);
        ARBWriter arbWriter = new ARBWriter(arbMap);
        arbWriter.arbFileWriter();
    }
}