package com.example.arbexcelconverterweb.domain.excel;

import com.example.arbexcelconverterweb.application.ExcelReader;

import java.util.LinkedHashMap;
import java.util.Map;

public class FakeExcelReader implements ExcelReader {
    @Override
    public Map<String, Map<String, String>> read() {

        Map<String, String> map = new LinkedHashMap<>();
        map.put("Key", "en");
        map.put("genericUpdate", "Update");
        map.put("profileBiography", "Biography");
        map.put("profileBioEmptyMessage", "Help the community know you better!");
        map.put("profileUpdateError", "Failed to update profile, are you connected to the internet?");
        map.put("alert_errors_found", "Errors found Please fix the following errors: {errors}");
        map.put("@alert_errors_found$#placeholders$#errors$#type$#String$#example", "Please fix the following errors: {errors}");
        map.put("alert_impersonation_notice", "You are currently impersonating {user} / {id}");
        map.put("@alert_impersonation_notice$#placeholders$#id$#type$#String$#example", "You are currently impersonating {user} / {id}");
        map.put("@alert_impersonation_notice$#placeholders$#user$#type$#String$#example", "You are currently impersonating {user} / {id}");

        return Map.of("en", map);
    }
}
