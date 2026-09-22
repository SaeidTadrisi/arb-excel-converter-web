package com.example.arbexcelconverterweb.presentation.dto;

import lombok.Getter;

import java.io.File;
import java.util.List;

public class FilesRequest {
    private List<File> filesList;
    private String referenceFile;

    public FilesRequest(List<File> filesList, String referenceFile) {
        this.filesList = filesList;
        this.referenceFile = referenceFile;
    }

    public List<File> getFilesList() {
        return filesList;
    }

    public void setFilesList(List<File> filesList) {
        this.filesList = filesList;
    }

    public String getReferenceFile() {
        return referenceFile;
    }

    public void setReferenceFile(String referenceFile) {
        this.referenceFile = referenceFile;
    }
}
