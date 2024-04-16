package com.example.arbexcelconverterweb.application;

import com.example.arbexcelconverterweb.domain.arb.ARBFile;
import com.example.arbexcelconverterweb.presentation.dto.ArbRequest;
import com.example.arbexcelconverterweb.presentation.dto.ExelResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadArbAndConvertToExcel {

    public ExelResponse conventToExcel(ArbRequest arbRequest) {
        ARBFile arbFile = new ARBFile(arbRequest.getFileList(), "S");
        List<String> strings = arbFile.getARBStringFiles();
        //
        return new ExelResponse();
        //
    }
}
