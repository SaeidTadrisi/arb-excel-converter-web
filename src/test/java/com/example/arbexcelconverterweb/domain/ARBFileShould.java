package com.example.arbexcelconverterweb.domain;

import com.example.arbexcelconverterweb.domain.exception.InvalidFileExtensionException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ARBFileShould {
    @Test
    void should_convert_arb_file_to_string() {
        //todo writ arb file in list -> list of......

        File file = new File("intl_en_test.arb");
        ARBFile arbFile = new ARBFile(List.of(file));
        List<String> strings = arbFile.arbToString();

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
                  }""";

        assertDoesNotThrow(arbFile::arbToString);
        Assertions.assertThat(strings.getFirst()).isEqualTo(outputFile);

    }

    @Test
    void should_Throws_exception_when_file_extension_is_not_valid() {
        //todo writ arb file in list -> list of......
        assertThrows(InvalidFileExtensionException.class, () -> new ARBFile(List.of()));
    }


}