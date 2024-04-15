package com.example.arbexcelconverterweb.presentation;

import com.example.arbexcelconverterweb.application.ReadArbAndConvertToExcel;
import com.example.arbexcelconverterweb.presentation.dto.ArbRequest;
import com.example.arbexcelconverterweb.presentation.dto.ExelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class ArbController {
    private final ReadArbAndConvertToExcel readArbAndConvertToExcel;

    @GetMapping("/arb")
    public ResponseEntity<ExelResponse> readArb(@RequestBody ArbRequest arbRequest) {
        ExelResponse exelResponse = readArbAndConvertToExcel.conventToExcel(arbRequest);
        return ResponseEntity.ok(exelResponse);
    }
}
