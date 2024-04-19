package com.example.arbexcelconverterweb.application;

import com.example.arbexcelconverterweb.domain.arb.ARBToExcelOrchestrator;

import java.io.File;
import java.util.List;

public class PrepareToTranslate {

    FilesReader filesReader;

    public PrepareToTranslate(FilesReader filesReader) {
        this.filesReader = filesReader;
    }

    public File makeOutput(){
        List<String> stringList = filesReader.read();
        ARBToExcelOrchestrator arbToExcelOrchestrator = new ARBToExcelOrchestrator(stringList);
        return arbToExcelOrchestrator.getExcelFile();
    }
}
