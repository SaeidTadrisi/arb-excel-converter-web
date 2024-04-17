package com.example.arbexcelconverterweb.domain.arb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SimpleElementsTest {

    List<String> stringFiles;
    ARBReader arbReader;
    Map<String, String> simpleMap;

    @BeforeEach
    void setUp() {
        File file = new File("intl_en_test.arb");
        arbReader = new ARBReader(List.of(file), "intl_en_test.arb");
        stringFiles = arbReader.getARBStringFiles();

        SimpleElements simpleElements = new SimpleElements(stringFiles.getFirst());
        simpleMap = simpleElements.otherElementsExtractor();
    }

    @Test
    void should_create_map_from_simple_elements() {
        Map<String, String> expectedMap = Map.of("Key", "en"
                ,"genericUpdate", "Update"
                ,"profileBiography", "Biography"
                ,"profileBioEmptyMessage", "Help the community know you better!"
                ,"profileUpdateError", "Failed to update profile, are you connected to the internet?"
                ,"alert_errors_found", "Errors found Please fix the following errors: {errors}"
                ,"alert_impersonation_notice", "You are currently impersonating {user} / {id}");

        assertThat(simpleMap).isEqualTo(expectedMap);
    }
}