package com.example.arbexcelconverterweb.domain;

import com.example.arbexcelconverterweb.domain.arb.ARBFile;
import com.example.arbexcelconverterweb.domain.arb.ElementsCombiner;
import com.example.arbexcelconverterweb.domain.arb.PlaceHolders;
import com.example.arbexcelconverterweb.domain.arb.SimpleElements;
import com.example.arbexcelconverterweb.domain.exception.InvalidFileExtensionException;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ARBFileShould {

    List<String> stringFiles;
    ARBFile arbFile;
    Map<String, String> simpleMap;
    Map<String, String> placeHolderMap;

    @BeforeEach
    void setUp() {
        File file = new File("intl_en_test.arb");
        arbFile = new ARBFile(List.of(file), "intl_en_test.arb");
        stringFiles = arbFile.getARBStringFiles();

        SimpleElements simpleElements = new SimpleElements();
        simpleMap = simpleElements.otherElementsExtractor(stringFiles.getFirst());

        PlaceHolders placeHolders = new PlaceHolders();
        placeHolderMap = placeHolders.placeHoldersExtractor(stringFiles.getFirst());
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
                  }
                }""".replace("\n", "\r\n");;

        assertDoesNotThrow(arbFile::getARBStringFiles);
        assertThat(stringFiles.getFirst()).isEqualTo(outputFile);

    }

    @Test
    void should_throws_exception_when_file_extension_is_not_valid() {
        File file = new File("test.txt");
        assertThrows(InvalidFileExtensionException.class, () -> new ARBFile(List.of(file), "test.txt"));
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

    @Test
    void should_combine_simple_elements_and_placeholder_maps() {
        ElementsCombiner elementsCombiner = new ElementsCombiner();

        Map<String, String> combinedMap = elementsCombiner.placeHolderTypeReplacer(simpleMap, placeHolderMap);

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

    @Test
    void should_sort_files_according_to_reference_file() {

        File file1 = new File("intl_en_test.arb");
        File file2 = new File("intl_es_test.arb");
        ARBFile arbFiles = new ARBFile(List.of(file1, file2), "intl_es_test.arb");
        List<String> stringFiles = arbFiles.getARBStringFiles();

        String outputFile = """
                {
                  "@@locale": "es",
                  "genericUpdate": "Actualizar",
                  "profileBiography": "Biografía",
                  "profileBioEmptyMessage": "¡Ayuda a la comunidad a conocerte mejor!",
                  "profileUpdateError": "No se pudo actualizar el perfil, ¿estás conectado a Internet?",
                  "alert_errors_found": "Errores encontrados Corrija los siguientes errores: {errors}",
                  "@alert_errors_found": {
                    "placeholders": {
                      "errors": {
                        "type": "String",
                        "example": "Please fix the following errors: {errors}"
                      }
                    }
                  },
                  "alert_impersonation_notice": "Actualmente estás haciéndote pasar por {user} / {id}",
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
                }""".replace("\n", "\r\n");

        assertThat(stringFiles.getFirst()).isEqualTo(outputFile);

    }
}