package com.example.arbexcelconverterweb.presentation.dto;

import java.io.File;

public class ExcelResponse {

    File excelFile;

    public ExcelResponse(File excelFile) {
        this.excelFile = excelFile;
    }

    public File getExcelFile() {
        return excelFile;
    }

    public void setExcelFile(File excelFile) {
        this.excelFile = excelFile;
    }
}
