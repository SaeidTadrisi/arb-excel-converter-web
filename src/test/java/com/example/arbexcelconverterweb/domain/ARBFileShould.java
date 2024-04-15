package com.example.arbexcelconverterweb.domain;

import com.example.arbexcelconverterweb.domain.exception.InvalidFileExtensionException;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ARBFileShould {
    @Test
    void should_convert_arb_file_to_string() {
        //todo writ arb file in list -> list of......
        ARBFile arbFile = new ARBFile(List.of());

        assertDoesNotThrow(arbFile::arbToString);
    }

    @Test
    void should_Throws_extcption_when_file_extension_is_not_valid() {
        //todo writ arb file in list -> list of......
        assertThrows(InvalidFileExtensionException.class, () -> new ARBFile(List.of()));
    }


}