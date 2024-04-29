package com.example.arbexcelconverterweb.domain.arb;

import com.example.arbexcelconverterweb.domain.exception.FileInUseException;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.lang.System.getProperty;
@Log4j2
public class ExcelWriter {
 
    private final List<Map<String, String>> combinedMap;

    public ExcelWriter(List<Map<String, String>> combinedMap) {
        this.combinedMap = combinedMap;
    }

    byte[] exportExcelFile() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");
            int colIdx = 0;
            for (int mapIdx = 0; mapIdx < combinedMap.size(); mapIdx++) {
                Map<String, String> map = combinedMap.get(mapIdx);

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
                workbook.write(bos);
                return bos.toByteArray();
        } catch (FileInUseException | IOException e) {
            log.error("You haven't permission or Excel file is open.");
            return new byte[0];
        }
    }
}
