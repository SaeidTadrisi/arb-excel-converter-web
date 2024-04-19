package com.example.arbexcelconverterweb.domain.arb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ARBElementsCombinerTest {

    List<String> stringFiles;
    Map<String, String> simpleMap;
    Map<String, String> placeHolderMap;
    Map<String, String> combinedMap;

    @BeforeEach
    void setUp() {
        stringFiles = new FakeFilesReader().read();

        ARBSimpleElementsExtractor ARBSimpleElementsExtractor = new ARBSimpleElementsExtractor(stringFiles.getFirst());
        simpleMap = ARBSimpleElementsExtractor.otherElementsExtractor();

        ARBPlaceHoldersExtractor ARBPlaceHoldersExtractor = new ARBPlaceHoldersExtractor(stringFiles.getFirst());
        placeHolderMap = ARBPlaceHoldersExtractor.placeHoldersExtractor();

        ARBElementsCombiner ARBElementsCombiner = new ARBElementsCombiner(simpleMap, placeHolderMap);
        combinedMap = ARBElementsCombiner.finalPatternedMap();
    }

    @Test
    void should_combine_simple_elements_and_placeholder_maps() {
        Map<String, Object> expectedMap = Map.of(
                "Key", "en",
                "genericUpdate", "Update",
                "profileBiography", "Biography",
                "profileBioEmptyMessage", "Help the community know you better!",
                "profileUpdateError", "Failed to update profile, are you connected to the internet?",
                "alert_errors_found", "Errors found Please fix the following errors: {errors}",
                "@alert_errors_found$#placeholders$#errors$#type$#String$#example", "Please fix the following errors: {errors}",
                "alert_impersonation_notice", "You are currently impersonating {user} / {id}",
                "@alert_impersonation_notice$#placeholders$#id$#type$#String$#example", "You are currently impersonating {user} / {id}",
                "@alert_impersonation_notice$#placeholders$#user$#type$#String$#example", "You are currently impersonating {user} / {id}");
        assertThat(combinedMap).isEqualTo(expectedMap);
    }
}