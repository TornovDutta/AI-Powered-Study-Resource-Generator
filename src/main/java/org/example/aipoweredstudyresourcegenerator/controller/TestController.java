package org.example.aipoweredstudyresourcegenerator.controller;


import org.example.aipoweredstudyresourcegenerator.Model.Status;
import org.example.aipoweredstudyresourcegenerator.Model.TestRequested;
import org.example.aipoweredstudyresourcegenerator.Model.Questions;
import org.example.aipoweredstudyresourcegenerator.config.DynamicSchedule;
import org.example.aipoweredstudyresourcegenerator.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import java.util.List;

@RestController
@RequestMapping("/tests/")
public class TestController {
    String topic;
    @Autowired
    private TestService service;
    @Autowired
    private DynamicSchedule dynamicScheduler;
    @PostMapping("")
    public ResponseEntity<List<Questions>> create(@RequestBody String topic){
        return new ResponseEntity<>(service.testGenerator(topic), HttpStatus.OK);
    }
    @PostMapping("/schedule")
    public ResponseEntity<Status> createTest(@RequestBody TestRequested testRequested){
        topic = testRequested.getTopic();
        LocalDateTime dateTime = LocalDateTime.of(testRequested.getDate(), testRequested.getTime());

        dynamicScheduler.schedule(() -> {

            List<Questions> questions=service.testGenerator(topic);
            service.sendMail(topic,questions);
        }, dateTime);
        Status status=new Status("scheduled", testRequested.getTopic());

        return new ResponseEntity<>(status, HttpStatus.OK);
    }


    @DeleteMapping("/schedule")
    public ResponseEntity<String> stopTest(){
        boolean stopped = dynamicScheduler.stop();
        topic = "";
        if(stopped) {
            Status status=new Status("success","Scheduled test stopped successfully");
            return new ResponseEntity<>("Scheduler stopped", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No active scheduler to stop", HttpStatus.OK);
        }
    }
}
