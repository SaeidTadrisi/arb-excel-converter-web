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

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/translate")
public class PrepareToTranslateController {

    @Autowired
    private ServletContext servletContext;

    @PostMapping("/prepare-excel")
    public ResponseEntity<byte[]> translateFiles(@RequestParam("fileList") List<MultipartFile> files,
                                                 @RequestParam("referenceFile") String referenceFile) {


        String realPath = servletContext.getRealPath("/");

        List<File> inMemoryFiles = files.stream()
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

        PrepareToTranslate prepareToTranslate = new PrepareToTranslate(
                new FilesReaderImpl(inMemoryFiles, referenceFile));

        byte[] excelData = prepareToTranslate.makeOutput();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("output.xlsx").build());
        
        return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    }
}