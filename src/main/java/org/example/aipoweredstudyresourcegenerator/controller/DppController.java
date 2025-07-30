package org.example.aipoweredstudyresourcegenerator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DppController {
    @GetMapping("/dpp")
    public String createDpp() {
        return "DPP";
    }
}
