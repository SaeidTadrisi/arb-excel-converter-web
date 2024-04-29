package com.example.arbexcelconverterweb.application;

import com.example.arbexcelconverterweb.domain.excel.ExcelReader;
import com.example.arbexcelconverterweb.domain.excel.ExcelToARBOrchestrator;

import java.util.List;
import java.util.Map;

public class ConvertTranslation {

    ExcelReader excelReader;

    public ConvertTranslation(ExcelReader excelReader) {
        this.excelReader = excelReader;
    }

    public List<byte[]> makeOutput(){
        Map<String, Map<String, String>> stringMap = excelReader.read();
        ExcelToARBOrchestrator excelToARBOrchestrator = new ExcelToARBOrchestrator(stringMap);
        return excelToARBOrchestrator.getArbFiles();
    }
}
