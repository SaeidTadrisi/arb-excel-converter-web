package com.example.arbexcelconverterweb.controller;

import com.example.arbexcelconverterweb.application.ConvertTranslation;
import com.example.arbexcelconverterweb.infrastructure.ExcelReaderImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.Objects.*;

@RestController
@RequestMapping("/translate")
public class ConvertTranslationController {

    @PostMapping("/convert")
    public ResponseEntity<List<String>> convertExcelToArb(@RequestParam("file") MultipartFile excelFile) {

        File excel = new File(requireNonNull(excelFile.getOriginalFilename()));

        ConvertTranslation convertTranslation = new ConvertTranslation(new ExcelReaderImpl(excel));

        List<byte[]> bytes = convertTranslation.makeOutput();

        for (byte[] aByte : bytes) {
            String s = new String(aByte);
            System.out.println(s);
        }




        return ResponseEntity.ok(List.of("bytes"));
    }
}