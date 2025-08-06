package org.example.aipoweredstudyresourcegenerator.controller;


import org.example.aipoweredstudyresourcegenerator.Model.Questions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("create")
    public ResponseEntity<Questions> create(@RequestBody String topic){
        return new ResponseEntity<>(new Questions(), HttpStatus.OK);
    }
}
