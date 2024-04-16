package com.example.arbexcelconverterweb.application;

import com.example.arbexcelconverterweb.domain.arb.ARBReader;
import com.example.arbexcelconverterweb.presentation.dto.ArbRequest;
import com.example.arbexcelconverterweb.presentation.dto.ExelResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadArbAndConvertToExcel {

    public ExelResponse conventToExcel(ArbRequest arbRequest) {
        ARBReader arbReader = new ARBReader(arbRequest.getFileList(), "S");
        List<String> strings = arbReader.getARBStringFiles();
        //
        return new ExelResponse();
        //
    }
}
