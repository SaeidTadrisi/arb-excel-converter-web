package com.example.arbexcelconverterweb.application;

import com.example.arbexcelconverterweb.domain.arb.ARBToExcelOrchestrator;
import com.example.arbexcelconverterweb.domain.arb.FilesReader;

import java.util.List;


public class PrepareToTranslate {

    FilesReader filesReader;

    public PrepareToTranslate(FilesReader filesReader) {
        this.filesReader = filesReader;
    }

    public byte[] makeOutput(){
        List<String> stringList = filesReader.read();
        ARBToExcelOrchestrator arbToExcelOrchestrator = new ARBToExcelOrchestrator(stringList);
        return arbToExcelOrchestrator.getExcelFile();
    }
}
