package com.example.arbexcelconverterweb.domain;

import com.example.arbexcelconverterweb.domain.exception.FileInUseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExportWriter {

    String userPath = System.getProperty("user.home") + File.separator;

    public void arbFileWriter(Map<String, String> arbMap) {
        for (Map.Entry<String, String> languagesMap : arbMap.entrySet()) {
            File outputFile = new File(userPath, languagesMap.getKey() + ".arb");
            try (FileWriter fileWriter = new FileWriter(outputFile)) {
                fileWriter.write(languagesMap.getValue());
            } catch (IOException e) {
                throw new IllegalArgumentException("No write permissions for the directory.");
            }
        }
    }

    public void excelFileWriter(List<Map<String, String>> excelListMap) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");
            int colIdx = 0;
            for (int mapIdx = 0; mapIdx < excelListMap.size(); mapIdx++) {
                Map<String, String> map = excelListMap.get(mapIdx);

                int rowIdx = 0;
                if (mapIdx == 0) {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        Row row = sheet.getRow(rowIdx);
                        if (row == null) {
                            row = sheet.createRow(rowIdx);
                        }
                        Cell keyCell = row.createCell(colIdx);
                        keyCell.setCellValue(key);

                        Cell valueCell = row.createCell(colIdx + 1);
                        valueCell.setCellValue(value);
                        rowIdx++;
                    }
                } else {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        String entryValue = entry.getValue();
                        Row row = sheet.getRow(rowIdx);
                        if (row != null) {
                            int lastCellNum = row.getLastCellNum();
                            Cell valueCell = row.createCell(lastCellNum);
                            valueCell.setCellValue(entryValue);
                            rowIdx++;
                        }
                    }
                }
            }
            File outputFile = new File(userPath, "output.xlsx");
            try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                workbook.write(fileOutputStream);
            }
        } catch (FileInUseException | IOException e) {
            throw new IllegalArgumentException("You haven't permission or Excel file is open.");
        }
    }
}
