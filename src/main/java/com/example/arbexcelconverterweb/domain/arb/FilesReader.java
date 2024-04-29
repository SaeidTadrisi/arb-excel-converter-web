package com.example.arbexcelconverterweb.domain.arb;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FilesReader {

    List<String> read ();
}
