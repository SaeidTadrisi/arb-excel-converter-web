package com.example.arbexcelconverterweb.domain.arb;

import com.example.arbexcelconverterweb.application.FilesReader;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ARBPlaceHoldersExtractorTest {

    List<String> stringFiles;
    Map<String, String> placeHolderMap;

    @BeforeEach
    void setUp() {
        File file = new File("intl_en_test.arb");
        stringFiles = new FakeFilesReader().read();

        var ARBPlaceHoldersExtractor = new ARBPlaceHoldersExtractor(stringFiles.getFirst());
        placeHolderMap = ARBPlaceHoldersExtractor.placeHoldersExtractor();
    }

    @Test
    void should_create_map_from_placeholders() throws JSONException {
        Map<String, Object> expectedMap = Map.of("@alert_errors_found$#placeholders$#errors$#type", "String"
                ,"@alert_errors_found$#placeholders$#errors$#example", "Please fix the following errors: {errors}"
                ,"@@locale", "en"
                ,"@alert_impersonation_notice$#placeholders$#id$#type", "String"
                ,"@alert_impersonation_notice$#placeholders$#id$#example", "You are currently impersonating {user} / {id}"
                ,"@alert_impersonation_notice$#placeholders$#user$#type", "String"
                ,"@alert_impersonation_notice$#placeholders$#user$#example", "You are currently impersonating {user} / {id}");

        assertThat(placeHolderMap).isEqualTo(expectedMap);
    }
}