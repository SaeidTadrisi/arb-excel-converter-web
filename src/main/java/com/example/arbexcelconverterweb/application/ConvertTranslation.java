package com.example.arbexcelconverterweb.application;

import com.example.arbexcelconverterweb.domain.excel.ExcelReader;
import com.example.arbexcelconverterweb.domain.excel.ExcelToARBOrchestrator;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ConvertTranslation {

    ExcelReader excelReader;

    public ConvertTranslation(ExcelReader excelReader) {
        this.excelReader = excelReader;
    }

    public List<File> makeOutput(){
        Map<String, Map<String, String>> stringMap = excelReader.read();
        ExcelToARBOrchestrator excelToARBOrchestrator = new ExcelToARBOrchestrator(stringMap);
        return excelToARBOrchestrator.getArbFiles();
    }
}
