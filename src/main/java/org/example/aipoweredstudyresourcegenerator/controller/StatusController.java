package org.example.aipoweredstudyresourcegenerator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {
    @GetMapping("")
    public String status(){
        return "The application is running";
    }
}
