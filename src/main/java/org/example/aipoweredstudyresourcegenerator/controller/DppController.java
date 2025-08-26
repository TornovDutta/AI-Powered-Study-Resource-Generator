package org.example.aipoweredstudyresourcegenerator.controller;
import org.example.aipoweredstudyresourcegenerator.Model.DppRequested;
import org.example.aipoweredstudyresourcegenerator.Model.Questions;
import org.example.aipoweredstudyresourcegenerator.config.SchedulerConfig;
import org.example.aipoweredstudyresourcegenerator.service.DppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DppController {
    String topic="";


    @Autowired
    private DppService service;

    @GetMapping("create")
    public ResponseEntity<List<Questions>> create(@RequestBody String topic){
        return new ResponseEntity<>(service.dppGenerator(topic), HttpStatus.OK);
    }
    @PostMapping("dppStart")
    public ResponseEntity<String> createDpp(@RequestBody DppRequested dppRequested){
        topic=dppRequested.getTopic();

        return  new ResponseEntity<>("ok",HttpStatus.OK);
    }
    @Scheduled(cron = "0 0 10 * * *")
    public ResponseEntity<List<Questions>> createscheduler(){
        if(topic.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.OK);
        }
        service.sendMail(topic);
        return new ResponseEntity<>(service.dppGenerator(topic),HttpStatus.OK);
    }
    public String generatedCons(LocalTime time, LocalDate date){
        int minute=time.getMinute();
        int hour=time.getHour();
        int day=date.getDayOfMonth();
        int month=date.getMonthValue();
        int year=date.lengthOfYear();
        return String.format("0 %d %d %d %d ? %d", minute, hour, day, month, year);
    }

    @DeleteMapping("dppStop")
    public ResponseEntity<String> stopDpp(){
        topic=null;
        return  new ResponseEntity<>("ok",HttpStatus.OK);
    }
}
