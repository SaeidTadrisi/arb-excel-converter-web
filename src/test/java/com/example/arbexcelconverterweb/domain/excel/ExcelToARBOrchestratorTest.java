package com.example.arbexcelconverterweb.domain.excel;

import org.junit.jupiter.api.Test;

import java.io.File;

class ExcelToARBOrchestratorTest {

    @Test
    void should_make_output() {


        ExcelToARBOrchestrator excelToARBOrchestrator = new ExcelToARBOrchestrator(new FakeExcelReader().read());
        excelToARBOrchestrator.getArbFiles();
    }
}