package com.example.arbexcelconverterweb.domain;

import com.example.arbexcelconverterweb.domain.arb.*;
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

class ARBReaderTest {

    List<String> stringFiles;
    ARBReader arbReader;
    Map<String, String> simpleMap;
    Map<String, String> placeHolderMap;
    Map<String, String> combinedMap;

    @BeforeEach
    void setUp() {
        File file = new File("intl_en_test.arb");
        arbReader = new ARBReader(List.of(file), "intl_en_test.arb");
        stringFiles = arbReader.getARBStringFiles();

        SimpleElements simpleElements = new SimpleElements(stringFiles.getFirst());
        simpleMap = simpleElements.otherElementsExtractor();

        PlaceHolders placeHolders = new PlaceHolders(stringFiles.getFirst());
        placeHolderMap = placeHolders.placeHoldersExtractor();

        ElementsCombiner elementsCombiner = new ElementsCombiner(simpleMap, placeHolderMap);
        combinedMap = elementsCombiner.placeHolderTypeReplacer();
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

        assertDoesNotThrow(arbReader::getARBStringFiles);
        assertThat(stringFiles.getFirst()).isEqualTo(outputFile);

    }

    @Test
    void should_throws_exception_when_file_extension_is_not_valid() {
        File file = new File("test.txt");
        assertThrows(InvalidFileExtensionException.class, () -> new ARBReader(List.of(file), "test.txt"));
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
        ARBReader arbFiles = new ARBReader(List.of(file1, file2), "intl_es_test.arb");
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

    @Test
    void should_export_an_excel() {
        List<Map<String, String>> maps = List.of(combinedMap);
        ExcelWriter excelWriter = new ExcelWriter(maps);
        excelWriter.exportExcelFile();
    }
}