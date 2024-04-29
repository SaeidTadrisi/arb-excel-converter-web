package com.example.arbexcelconverterweb.domain.excel;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class ExcelReaderImplTest {

    @Test
    void should_convert_excel_file_to_string() {

        Map<String, Map<String, String>> excelStringFile = new FakeExcelReader().read();

        Map<String, Map<String, String>> excpectedMap = Map.of("en", Map.of("Key", "en",
                "genericUpdate", "Update",
                "profileBiography", "Biography",
                "profileBioEmptyMessage", "Help the community know you better!",
                "profileUpdateError", "Failed to update profile, are you connected to the internet?",
                "alert_errors_found", "Errors found Please fix the following errors: {errors}",
                "@alert_errors_found$#placeholders$#errors$#type$#String$#example", "Please fix the following errors: {errors}",
                "alert_impersonation_notice", "You are currently impersonating {user} / {id}",
                "@alert_impersonation_notice$#placeholders$#id$#type$#String$#example", "You are currently impersonating {user} / {id}",
                "@alert_impersonation_notice$#placeholders$#user$#type$#String$#example", "You are currently impersonating {user} / {id}"));

        assertThat(excelStringFile).isEqualTo(excpectedMap);
    }
}
