package com.example.arbexcelconverterweb.controller;

import com.example.arbexcelconverterweb.application.PrepareToTranslate;
import com.example.arbexcelconverterweb.infrastructure.FilesReaderImpl;
import org.springframework.http.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/translate")
public class PrepareToTranslateController {

    @PostMapping("/prepare-excel")
    public ResponseEntity<byte[]> translateFiles(@RequestParam("fileList") List<MultipartFile> files,
                                                 @RequestParam("referenceFile") String referenceFile) {

        List<File> inMemoryFiles = files.stream()
                .map(MultipartFile::getOriginalFilename)
                .map(Objects::requireNonNull)
                .map(File::new)
                .toList();

        PrepareToTranslate prepareToTranslate = new PrepareToTranslate(
                new FilesReaderImpl(inMemoryFiles, referenceFile));

        byte[] excelData = prepareToTranslate.makeOutput();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("output.xlsx").build());

        return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    }
}