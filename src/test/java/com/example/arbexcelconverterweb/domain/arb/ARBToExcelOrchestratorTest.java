package com.example.arbexcelconverterweb.domain.arb;

import com.example.arbexcelconverterweb.domain.excel.ExcelToARBOrchestrator;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

class ARBToExcelOrchestratorTest {

    @Test
    void should_make_output() {
        List<String> stringList = new FakeFilesReader().read();

        ARBToExcelOrchestrator arbToExcelOrchestrator = new ARBToExcelOrchestrator(stringList);
        arbToExcelOrchestrator.getExcelFile();
    }

}