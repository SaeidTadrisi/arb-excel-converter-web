package com.example.arbexcelconverterweb.domain;

import com.example.arbexcelconverterweb.domain.exception.FileNotFoundException;
import com.example.arbexcelconverterweb.domain.exception.InvalidFileExtensionException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.Files.readString;

public class ImportReader implements ImportReaders {

    private static final String FILE_NOT_FOUND = "File not found";
    public static final String INVALID_FILE_EXTENSION = "Invalid file extension";
    List<File> arbFileList;
    String referenceFile;
    File excelFile;

    public ImportReader(List<File> arbFileList, String referenceFile) {
        this.arbFileList = arbFileList;
        this.referenceFile = referenceFile;
    }

    public ImportReader(File excelFile) {
        this.excelFile = excelFile;
    }

    @Override
    public List<String> getARBStringFiles() {
        List<File> sortedList = fileListSorter(arbFileList, referenceFile);
        return arbToStringReader(sortedList);
    }

    private List<File> fileListSorter(List<File> arbFileList, String referenceFile) {
        List<File> copyOfFileList = new LinkedList<>(List.copyOf(arbFileList));
        List<File> sortedList = new LinkedList<>();
        for (File file : arbFileList) {
            if (file.getName().equals(referenceFile)) {
                sortedList.add(file);
                copyOfFileList.remove(file);
                sortedList.addAll(copyOfFileList);
            }
        }
        return sortedList;
    }

    private List<String> arbToStringReader(List<File> arbFiles) {
        List<String> stringFileList = new ArrayList<>();
        try {
            arbFileCheck(arbFiles);
        } catch (InvalidFileExtensionException e) {

        }
        for (File file : arbFiles) {
            try {
                Path path = Paths.get(file.getAbsolutePath());
                String stringFile = readString(path);
                stringFileList.add(stringFile);

            } catch (FileNotFoundException | IOException e) {
            }
        }
        return (stringFileList);
    }

    private void arbFileCheck(List<File> arbFiles) {
        for (File file : arbFiles) {
            String fileName = file.getName();
            String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!extensionName.equals("arb")) {
                throw new InvalidFileExtensionException();
            }
        }
    }

    @Override
    public Map<String, Map<String, String>> getExcelStringFile() {
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
        }
        return importData;
    }

    private InputStream getExcelFile() {
        InputStream xlsInputStream = null;
        try {
            xlsFileCheck(excelFile);
        } catch (InvalidFileExtensionException e) {
        }
        try {
            Path path = Paths.get(excelFile.getAbsolutePath());
            xlsInputStream = Files.newInputStream(path);
        } catch (FileNotFoundException | IOException e) {
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
