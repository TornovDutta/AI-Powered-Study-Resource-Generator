package org.example.aipoweredstudyresourcegenerator.controller;


import org.example.aipoweredstudyresourcegenerator.Model.Questions;
import org.example.aipoweredstudyresourcegenerator.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    private TestService service;
    @GetMapping("create")
    public ResponseEntity<List<Questions>> create(@RequestBody String topic){
        return new ResponseEntity<>(service.testGenerator(topic), HttpStatus.OK);
    }
}
