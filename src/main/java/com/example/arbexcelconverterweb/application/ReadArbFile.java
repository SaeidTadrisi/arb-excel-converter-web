package com.example.arbexcelconverterweb.application;

import com.example.arbexcelconverterweb.domain.arb.ARBReader;
import com.example.arbexcelconverterweb.presentation.dto.ArbRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadArbFile {

    public List<String> conventToExcel(ArbRequest arbRequest) {
        ARBReader arbReader = new ARBReader(arbRequest.getFileList(), "S");
        return arbReader.getARBStringFiles();
    }
}
