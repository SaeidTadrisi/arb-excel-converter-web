package com.example.arbexcelconverterweb.domain;

import java.util.List;
import java.util.Map;

public interface ImportReaders {
    List<String> getARBStringFiles();

    Map<String, Map<String, String>> getExcelStringFile();
}
