package com.example.arbexcelconverterweb.domain.excel;

import com.example.arbexcelconverterweb.domain.exception.FileNotFoundException;
import com.example.arbexcelconverterweb.domain.exception.InvalidFileExtensionException;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

@Log4j2
public class ExcelReader {

    private final File excelFile;

    public ExcelReader(File excelFile) {
        this.excelFile = excelFile;
        xlsFileCheck(excelFile);
    }

    Map<String, Map<String, String>> getExcelStringFile() {
        Map<String, Map<String, String>> importData = new LinkedHashMap<>();
        InputStream excelInputStream = getExcelFile();

        try (Workbook workbook = WorkbookFactory.create(excelInputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            int lastCellNum = sheet.getRow(0).getLastCellNum();
            for (int cellIndex = 1; cellIndex < lastCellNum; cellIndex++) {
                Map<String, String> importMap = new LinkedHashMap<>();
                String columnName = sheet.getRow(0).getCell(cellIndex).getStringCellValue();
                for (Row row : sheet) {
                    Cell keyCell = row.getCell(0);
                    Cell valueCell = row.getCell(cellIndex);
                    if (keyCell != null && valueCell != null) {
                        String key = keyCell.getStringCellValue();
                        String value = valueCell.getStringCellValue();
                        importMap.put(key, value);
                    } else if (keyCell != null) {
                        String key = keyCell.getStringCellValue();
                        String value = "";
                        importMap.put(key, value);
                    }
                }
                importData.put(columnName, importMap);
            }
        } catch (FileNotFoundException | NullPointerException | IOException e) {
            log.error("Excel File not found");

        }
        return importData;
    }

    private InputStream getExcelFile() {
        InputStream xlsInputStream = null;
        try {
            xlsFileCheck(excelFile);
        } catch (InvalidFileExtensionException e) {
            log.error("Invalid file extension");
        }
        try {
            Path path = Paths.get(excelFile.getAbsolutePath());
            xlsInputStream = Files.newInputStream(path);
        } catch (FileNotFoundException | IOException e) {
            log.error("Excel File not found");
        }
        return (xlsInputStream);
    }

    private void xlsFileCheck(File excelFile) {
        String extensionName = excelFile.getName().substring(excelFile.getName().lastIndexOf(".") + 1);
        if (!extensionName.equals("xls") && !extensionName.equals("xlsx")) {
            throw new InvalidFileExtensionException();
        }
    }
}
