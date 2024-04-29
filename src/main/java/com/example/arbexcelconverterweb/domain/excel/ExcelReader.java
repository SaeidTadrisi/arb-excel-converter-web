package com.example.arbexcelconverterweb.domain.excel;

import java.util.Map;

public interface ExcelReader {

    Map<String, Map<String, String>> read();
}
