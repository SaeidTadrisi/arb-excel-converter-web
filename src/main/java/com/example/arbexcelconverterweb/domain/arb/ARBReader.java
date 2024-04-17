package com.example.arbexcelconverterweb.domain.arb;

import com.example.arbexcelconverterweb.domain.exception.FileException;
import com.example.arbexcelconverterweb.domain.exception.InvalidFileExtensionException;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.Files.readString;

@Log4j2
public class ARBReader {

    private final List<File> arbFiles;
    private final String referenceFile;

    public ARBReader(List<File> arbFiles, String referenceFile) {
        arbFileCheck(arbFiles);
        this.referenceFile = referenceFile;
        this.arbFiles = arbFiles;
    }

    List<String> getARBStringFiles() {
        List<File> sortedList = fileListSorter(arbFiles, referenceFile);
        return arbToString(sortedList);
    }

    private List<String> arbToString(List<File> arbFiles) {
        return arbFiles.stream().map(File::getAbsolutePath)
                .map(Paths::get)
                .map(this::getReadString)
                .toList();
    }

    private List<File> fileListSorter(List<File> arbFiles, String referenceFile) {
        List<File> copyOfFileList = new LinkedList<>(List.copyOf(arbFiles));
        List<File> sortedList = new LinkedList<>();
        arbFiles.stream()
                .filter(file -> file.getName().equals(referenceFile))
                .forEach(file -> {
                    sortedList.add(file);
                    copyOfFileList.remove(file);
                    sortedList.addAll(copyOfFileList);
                });
        return sortedList;
    }

    private String getReadString(Path path) {
        try {
            return readString(path);
        } catch (IOException exception) {
            log.error(exception.getMessage());
            throw new FileException("File not found");
        }
    }

    private void arbFileCheck(List<File> arbFiles) {
        boolean match = arbFiles.stream()
                .map(File::getName)
                .allMatch(name -> name.substring(name.lastIndexOf(".") + 1).equals("arb"));
        if (!match)
            throw new InvalidFileExtensionException();
    }
}
