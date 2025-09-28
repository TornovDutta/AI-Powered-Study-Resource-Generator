package org.example.aipoweredstudyresourcegenerator.controller;
import org.example.aipoweredstudyresourcegenerator.Model.DppRequested;
import org.example.aipoweredstudyresourcegenerator.Model.Status;
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
@RequestMapping("/dpp")
public class DppController {
    String topic="";


    @Autowired
    private DppService service;
    @Autowired
    private DynamicSchedule dynamicScheduler;

    @GetMapping("")
    public ResponseEntity<List<Questions>> create(@RequestBody String topic){
        return new ResponseEntity<>(service.dppGenerator(topic), HttpStatus.OK);
    }
    @PostMapping("schedule")
    public ResponseEntity<Status> createDpp(@RequestBody DppRequested requested){
        topic = requested.getTopic();
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), requested.getTime());

        dynamicScheduler.schedule(() -> {

            List<Questions> questions=service.dppGenerator(topic);
            service.sendMail(topic,questions);
        }, dateTime);
        Status status=new Status("scheduled",topic);

        return new ResponseEntity<>(status, HttpStatus.OK);
    }


    @DeleteMapping("schedule")
    public ResponseEntity<Status> stopDpp(){
        boolean stopped = dynamicScheduler.stop();
        topic = "";
        if(stopped) {
            Status status=new Status("success","Scheduled DPP stopped successfully");
            return new ResponseEntity<>(status, HttpStatus.OK);
        } else {
            return null;
        }
    }
}
