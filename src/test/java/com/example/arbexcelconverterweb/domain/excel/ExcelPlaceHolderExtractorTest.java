package com.example.arbexcelconverterweb.domain.excel;

import com.example.arbexcelconverterweb.application.ExcelReaderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExcelPlaceHolderExtractorTest {

    Map<String, String> placeHolderMap;

    @BeforeEach
    void setUp() {
        Map<String, Map<String, String>> excelStringFile = new FakeExcelReader().read();

        ExcelPlaceHolderExtractor excelPlaceHolderExtractor = new ExcelPlaceHolderExtractor(excelStringFile.get("en"));
        placeHolderMap = excelPlaceHolderExtractor.placeHoldersExtractor();
    }

    @Test
    void should_create_map_from_placeholders() {
        Map<String, Object> expectedMap = Map.of(
                "@alert_errors_found$#placeholders$#errors$#type$#String$#example", "Please fix the following errors: {errors}"
                ,"@alert_impersonation_notice$#placeholders$#id$#type$#String$#example", "You are currently impersonating {user} / {id}"
                ,"@alert_impersonation_notice$#placeholders$#user$#type$#String$#example", "You are currently impersonating {user} / {id}");

        assertThat(placeHolderMap).isEqualTo(expectedMap);
    }

}