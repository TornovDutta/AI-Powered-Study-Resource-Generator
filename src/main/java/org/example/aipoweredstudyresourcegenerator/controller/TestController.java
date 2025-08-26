package org.example.aipoweredstudyresourcegenerator.controller;


import org.example.aipoweredstudyresourcegenerator.Model.Questions;
import org.example.aipoweredstudyresourcegenerator.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {
    String topic;
    @Autowired
    private TestService service;
    @GetMapping("create")
    public ResponseEntity<List<Questions>> create(@RequestBody String topic){
        return new ResponseEntity<>(service.testGenerator(topic), HttpStatus.OK);
    }
    @PostMapping("testStart")
    public ResponseEntity<String> createDpp(@RequestBody String t){
        topic=t;
        return  new ResponseEntity<>("ok",HttpStatus.OK);
    }
    @Scheduled(cron = "0 0 10 * * *")
    public ResponseEntity<List<Questions>> createscheduler(){
        if(topic.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.OK);
        }
        service.sendMail(topic);
        return new ResponseEntity<>(service.testGenerator(topic),HttpStatus.OK);
    }

    @DeleteMapping("testStop")
    public ResponseEntity<String> stopDpp(){
        topic=null;
        return  new ResponseEntity<>("ok",HttpStatus.OK);
    }
}
