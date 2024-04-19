package com.example.arbexcelconverterweb.domain.excel;

import com.example.arbexcelconverterweb.application.ExcelReaderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExcelElementsCombinerTest {

    Map<String, String> simpleMap;
    Map<String, String> placeHolderMap;
    String arbPatternedString;

    @BeforeEach
    void setUp() {
        Map<String, Map<String, String>> excelStringFile = new FakeExcelReader().read();

        var excelSimpleElementsExtractor = new ExcelSimpleElementsExtractor(excelStringFile.get("en"));
        simpleMap = excelSimpleElementsExtractor.otherElementsExtractor();

        var excelPlaceHolderExtractor = new ExcelPlaceHolderExtractor(excelStringFile.get("en"));
        placeHolderMap = excelPlaceHolderExtractor.placeHoldersExtractor();

        ExcelElementsCombiner excelElementsCombiner = new ExcelElementsCombiner(simpleMap, placeHolderMap);
        arbPatternedString = excelElementsCombiner.arbPatternedMap();
    }

    @Test
    void should_combine_simple_elements_and_placeholder_maps() {
        String excpectedString = """
                {
                  "@@locale": "en",
                  "genericUpdate": "Update",
                  "profileBiography": "Biography",
                  "profileBioEmptyMessage": "Help the community know you better!",
                  "profileUpdateError": "Failed to update profile, are you connected to the internet?",
                  "alert_errors_found": "Errors found Please fix the following errors: {errors}",
                  "@alert_errors_found": {
                    "placeholders": {
                      "errors": {
                        "type": "String",
                        "example": "Please fix the following errors: {errors}"
                      }
                    }
                  },
                  "alert_impersonation_notice": "You are currently impersonating {user} / {id}",
                  "@alert_impersonation_notice": {
                    "placeholders": {
                      "id": {
                        "type": "String",
                        "example": "You are currently impersonating {user} / {id}"
                      },
                      "user": {
                        "type": "String",
                        "example": "You are currently impersonating {user} / {id}"
                      }
                    }
                  }
                }""";

        assertThat(arbPatternedString).isEqualTo(excpectedString);
    }
}