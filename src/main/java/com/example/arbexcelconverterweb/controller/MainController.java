package com.example.arbexcelconverterweb.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class MainController {

    @GetMapping("/*")
    public String sayHello() {
        return "Welcome To ARB Converter Web App ;)";
    }
}

