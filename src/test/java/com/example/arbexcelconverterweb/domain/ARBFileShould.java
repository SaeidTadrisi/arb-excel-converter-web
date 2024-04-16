package com.example.arbexcelconverterweb.domain;

import com.example.arbexcelconverterweb.domain.arb.ARBFile;
import com.example.arbexcelconverterweb.domain.arb.PlaceHolders;
import com.example.arbexcelconverterweb.domain.arb.SimpleElements;
import com.example.arbexcelconverterweb.domain.exception.InvalidFileExtensionException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ARBFileShould {

    List<String> stringFiles;
    ARBFile arbFile;

    @BeforeEach
    void setUp() {
        File file = new File("intl_en_test.arb");
        arbFile = new ARBFile(List.of(file));
        stringFiles = arbFile.arbToString();
    }

    @Test
    void should_convert_arb_file_to_string() {
        String outputFile = """
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
                  },
                }""".replace("\n", "\r\n");;

        assertDoesNotThrow(arbFile::arbToString);
        assertThat(stringFiles.getFirst()).isEqualTo(outputFile);

    }

    @Test
    void should_throws_exception_when_file_extension_is_not_valid() {
        File file = new File("test.txt");
        assertThrows(InvalidFileExtensionException.class, () -> new ARBFile(List.of(file)));
    }

    @Test
    void should_create_map_from_simple_elements() {
        SimpleElements simpleElements = new SimpleElements();
        Map<String, String> stringMap = simpleElements.otherElementsExtractor(stringFiles.getFirst());

        Map<String, String> expectedMap = Map.of("Key", "en"
                ,"genericUpdate", "Update"
                ,"profileBiography", "Biography"
                ,"profileBioEmptyMessage", "Help the community know you better!"
                ,"profileUpdateError", "Failed to update profile, are you connected to the internet?"
                ,"alert_errors_found", "Errors found Please fix the following errors: {errors}"
                ,"alert_impersonation_notice", "You are currently impersonating {user} / {id}");

        assertThat(stringMap).isEqualTo(expectedMap);
    }

    @Test
    void should_create_map_from_placeholders() throws JSONException {
        PlaceHolders placeHolders = new PlaceHolders();
        Map<String, String> stringObjectMap = placeHolders.placeHoldersExtractor(stringFiles.getFirst());

        Map<String, Object> expectedMap = Map.of("@alert_errors_found$#placeholders$#errors$#type", "String"
                ,"@alert_errors_found$#placeholders$#errors$#example", "Please fix the following errors: {errors}"
                ,"@@locale", "en"
                ,"@alert_impersonation_notice$#placeholders$#id$#type", "String"
                ,"@alert_impersonation_notice$#placeholders$#id$#example", "You are currently impersonating {user} / {id}"
                ,"@alert_impersonation_notice$#placeholders$#user$#type", "String"
                ,"@alert_impersonation_notice$#placeholders$#user$#example", "You are currently impersonating {user} / {id}");

        assertThat(stringObjectMap).isEqualTo(expectedMap);

    }
}