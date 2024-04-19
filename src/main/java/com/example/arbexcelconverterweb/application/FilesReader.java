package com.example.arbexcelconverterweb.application;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public interface FilesReader {

    String read (List<File> fileList, String referenceFile);
}
