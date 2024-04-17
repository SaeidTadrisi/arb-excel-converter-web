package com.example.arbexcelconverterweb.application;

import com.example.arbexcelconverterweb.domain.arb.ArbFile;
import com.example.arbexcelconverterweb.presentation.dto.ArbRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadArbFile {

    public List<String> conventToExcel(ArbRequest arbRequest) {
        ArbFile arbReader = new ArbFile(arbRequest.getFileList(), "S");
        return arbReader.getARBStringFiles();
    }
}
