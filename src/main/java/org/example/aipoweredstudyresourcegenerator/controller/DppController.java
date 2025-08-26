package org.example.aipoweredstudyresourcegenerator.controller;
import org.example.aipoweredstudyresourcegenerator.Model.Questions;
import org.example.aipoweredstudyresourcegenerator.config.SchedulerConfig;
import org.example.aipoweredstudyresourcegenerator.service.DppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DppController {
    String topic="";

    @Autowired
    private DppService service;

    @Autowired
    private SchedulerConfig scheduler;
    @GetMapping("create")
    public ResponseEntity<List<Questions>> create(@RequestBody String topic){
        return new ResponseEntity<>(service.dppGenerator(topic), HttpStatus.OK);
    }
    @PostMapping("dppStart")
    public ResponseEntity<String> createDpp(@RequestBody String t){
        topic=t;
        return  new ResponseEntity<>("ok",HttpStatus.OK);
    }
    @Scheduled(cron = "0 0 10 * * *")
    public ResponseEntity<List<Questions>> createscheduler(){
        topic="java";
        return new ResponseEntity<>(service.dppGenerator(topic),HttpStatus.OK);
    }
}
