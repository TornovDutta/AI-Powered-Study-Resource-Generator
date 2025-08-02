package org.example.aipoweredstudyresourcegenerator.controller;

import org.example.aipoweredstudyresourcegenerator.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DppController {
    @Autowired
    private OpenAIService service;
    @GetMapping("/dpp")
    public String createDpp() {
        return service.getResponse("hello");
    }
}
