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

    public List<String> arbToString(List<File> arbFiles) {
        List<String> stringFileList = new ArrayList<>();
        arbFileCheck(arbFiles);
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
        boolean match = arbFiles.stream()
                .map(File::getName)
                .allMatch(name -> name.substring(name.lastIndexOf(".") + 1).equals("arb"));
        if (!match)
            throw new InvalidFileExtensionException();
    }
}
