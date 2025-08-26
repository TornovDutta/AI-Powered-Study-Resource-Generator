package org.example.aipoweredstudyresourcegenerator.controller;


import org.example.aipoweredstudyresourcegenerator.Model.DppRequested;
import org.example.aipoweredstudyresourcegenerator.Model.Questions;
import org.example.aipoweredstudyresourcegenerator.config.DynamicSchedule;
import org.example.aipoweredstudyresourcegenerator.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import java.util.List;

@RestController
public class TestController {
    String topic;
    @Autowired
    private TestService service;
    @Autowired
    private DynamicSchedule dynamicScheduler;
    @GetMapping("create")
    public ResponseEntity<List<Questions>> create(@RequestBody String topic){
        return new ResponseEntity<>(service.testGenerator(topic), HttpStatus.OK);
    }
    @PostMapping("testStart")
    public ResponseEntity<String> createTest(@RequestBody DppRequested dppRequested){
        topic = dppRequested.getTopic();
        LocalDateTime dateTime = LocalDateTime.of(dppRequested.getDate(), dppRequested.getTime());

        dynamicScheduler.schedule(() -> {

            List<Questions> questions=service.testGenerator(topic);
            service.sendMail(topic,questions);
        }, dateTime);

        return new ResponseEntity<>("Scheduled successfully", HttpStatus.OK);
    }


    @DeleteMapping("testStop")
    public ResponseEntity<String> stopTest(){
        boolean stopped = dynamicScheduler.stop();
        topic = "";
        if(stopped) {
            return new ResponseEntity<>("Scheduler stopped", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No active scheduler to stop", HttpStatus.OK);
        }
    }
}
