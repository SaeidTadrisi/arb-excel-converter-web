package com.example.arbexcelconverterweb.controller;

import com.example.arbexcelconverterweb.application.PrepareToTranslate;
import com.example.arbexcelconverterweb.domain.exception.FileException;
import com.example.arbexcelconverterweb.infrastructure.FilesReaderImpl;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/translate")
public class PrepareToTranslateController {

    private final ServletContext servletContext;

    public PrepareToTranslateController(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @PostMapping("/prepare-translate")
    public ResponseEntity<byte[]> translateFiles(@RequestParam("fileList") List<MultipartFile> files,
                                                 @RequestParam("referenceFile") String referenceFile) {

        List<File> inMemoryFiles = processUploadedFiles(files);

        PrepareToTranslate prepareToTranslate = new PrepareToTranslate(new FilesReaderImpl(inMemoryFiles, referenceFile));

        byte[] excelData = prepareToTranslate.makeOutput();
        return buildExcelResponse(excelData);
    }

    private List<File> processUploadedFiles(List<MultipartFile> files) {

        String realPath = servletContext.getRealPath("/");

        return files.stream()
                .map(file -> {
                    String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                    File destinationFile = new File(realPath + filename);
                    try {
                        file.transferTo(destinationFile);
                        return destinationFile;
                    } catch (IOException e) {
                        throw new FileException("Failed to store file " + filename);
                    }
                })
                .toList();
    }

    private static ResponseEntity<byte[]> buildExcelResponse(byte[] excelData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("output.xlsx").build());

        return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    }
}