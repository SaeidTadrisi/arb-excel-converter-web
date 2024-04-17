package com.example.arbexcelconverterweb.domain.arb;

import com.example.arbexcelconverterweb.domain.excel.ExcelToARBOrchestrator;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

class ARBToExcelOrchestratorTest {

    @Test
    void should_make_output() {

        List<File> fileList = List.of(
                new File("intl_en.arb"),
                new File("intl_es.arb"),
                new File("intl_fr.arb"));

        String referenceFile = "intl_es.arb";


        ARBToExcelOrchestrator arbToExcelOrchestrator = new ARBToExcelOrchestrator(fileList, referenceFile);
        arbToExcelOrchestrator.getExcelFile();
    }

}