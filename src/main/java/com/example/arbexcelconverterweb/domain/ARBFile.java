package com.example.arbexcelconverterweb.domain;

import com.example.arbexcelconverterweb.domain.exception.FileNotFoundException;
import com.example.arbexcelconverterweb.domain.exception.InvalidFileExtensionException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.readString;

public class ARBFile {

    private File arbFile;

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

}
