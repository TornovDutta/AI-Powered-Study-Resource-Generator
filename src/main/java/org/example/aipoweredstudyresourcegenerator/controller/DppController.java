package org.example.aipoweredstudyresourcegenerator.controller;
import org.example.aipoweredstudyresourcegenerator.Model.DppRequested;
import org.example.aipoweredstudyresourcegenerator.Model.TestRequested;
import org.example.aipoweredstudyresourcegenerator.Model.Questions;
import org.example.aipoweredstudyresourcegenerator.config.DynamicSchedule;

import org.example.aipoweredstudyresourcegenerator.service.DppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class DppController {
    String topic="";


    @Autowired
    private DppService service;
    @Autowired
    private DynamicSchedule dynamicScheduler;

    @GetMapping("create")
    public ResponseEntity<List<Questions>> create(@RequestBody String topic){
        return new ResponseEntity<>(service.dppGenerator(topic), HttpStatus.OK);
    }
    @PostMapping("dppStart")
    public ResponseEntity<String> createDpp(@RequestBody DppRequested requested){
        topic = requested.getTopic();
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), requested.getTime());

        dynamicScheduler.schedule(() -> {

            List<Questions> questions=service.dppGenerator(topic);
            service.sendMail(topic,questions);
        }, dateTime);

        return new ResponseEntity<>("Scheduled successfully", HttpStatus.OK);
    }


    @DeleteMapping("dppStop")
    public ResponseEntity<String> stopDpp(){
        boolean stopped = dynamicScheduler.stop();
        topic = "";
        if(stopped) {
            return new ResponseEntity<>("Scheduler stopped", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No active scheduler to stop", HttpStatus.OK);
        }
    }
}
