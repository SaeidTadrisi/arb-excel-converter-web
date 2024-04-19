package com.example.arbexcelconverterweb.application;

import com.example.arbexcelconverterweb.domain.exception.FileException;
import com.example.arbexcelconverterweb.domain.exception.InvalidFileExtensionException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static java.nio.file.Files.readString;

public class FilesReaderImpl implements FilesReader {

    @Override
    public List<String> read(List<File> fileList, String referenceFile) {
        List<File> sortedList = fileListSorter(fileList, referenceFile);
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
