package com.example.arbexcelconverterweb.controller;

import com.example.arbexcelconverterweb.application.ConvertTranslation;
import com.example.arbexcelconverterweb.domain.exception.FileException;
import com.example.arbexcelconverterweb.infrastructure.ExcelReaderImpl;
import jakarta.servlet.ServletContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.util.Objects.requireNonNull;

@RestController
@CrossOrigin(origins = "https://saeidtadrisi.github.io/arb-converter/", methods = { RequestMethod.POST }, allowedHeaders = { "Content-Type" })
@RequestMapping("/translate")
public class ConvertTranslationController {

    private final ServletContext servletContext;

    public ConvertTranslationController(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @PostMapping("/convert-translation")
    public ResponseEntity<byte[]> convertExcelToArb(@RequestParam("file") MultipartFile excelFile) {

        File inMemoryFile = processUploadedFiles(excelFile);

        ConvertTranslation convertTranslation = new ConvertTranslation(new ExcelReaderImpl(inMemoryFile));

        List<byte[]> bytesList = convertTranslation.makeOutput();

        return buildZipFileResponse(bytesList);
    }

    private File processUploadedFiles(MultipartFile excelFile) {
        String realPath = servletContext.getRealPath("/");
        String filename = StringUtils.cleanPath(requireNonNull(excelFile.getOriginalFilename()));
        File inMemoryFile = new File(realPath + filename);

        try {
            excelFile.transferTo(inMemoryFile);
        } catch (IOException e) {
            throw new FileException("Failed to store file " + filename);
        }
        return inMemoryFile;
    }

    private static ResponseEntity<byte[]> buildZipFileResponse(List<byte[]> bytesList) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (int i = 0; i < bytesList.size(); i++) {
                byte[] bytes = bytesList.get(i);
                ZipEntry zipEntry = new ZipEntry("output_" + i + ".arb");
                zos.putNextEntry(zipEntry);
                zos.write(bytes);
                zos.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] zipBytes = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("filename", "output.zip");

        return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);
    }
}