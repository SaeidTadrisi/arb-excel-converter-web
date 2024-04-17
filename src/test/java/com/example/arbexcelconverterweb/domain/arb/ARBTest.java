package com.example.arbexcelconverterweb.domain.arb;

import com.example.arbexcelconverterweb.domain.exception.InvalidFileExtensionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ARBTest {

    List<String> stringFiles;
    ArbFile arbFile;
    Map<String, String> simpleMap;
    Map<String, String> placeHolderMap;
    Map<String, String> combinedMap;

    @BeforeEach
    void setUp() {
        File file = new File("intl_en_test.arb");
        arbFile = new ArbFile(List.of(file), "intl_en_test.arb");
        stringFiles = arbFile.getARBStringFiles();

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

        assertDoesNotThrow(arbFile::getARBStringFiles);
        assertThat(stringFiles.getFirst()).isEqualTo(outputFile);
    }

    @Test
    void should_throws_exception_when_file_extension_is_not_valid() {
        File file = new File("test.txt");
        assertThrows(InvalidFileExtensionException.class, () -> new ArbFile(List.of(file), "test.txt"));
    }

    @Test
    void should_sort_files_according_to_reference_file() {

        File file1 = new File("intl_en_test.arb");
        File file2 = new File("intl_es_test.arb");
        ArbFile arbFiles = new ArbFile(List.of(file1, file2), "intl_es_test.arb");
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