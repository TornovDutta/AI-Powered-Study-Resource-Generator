package org.example.aipoweredstudyresourcegenerator.controller;
import org.example.aipoweredstudyresourcegenerator.Model.Questions;
import org.example.aipoweredstudyresourcegenerator.service.DppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DppController {

    @Autowired
    private DppService service;
    @GetMapping("create")
    public ResponseEntity<List<Questions>> create(@RequestBody String topic){
        return new ResponseEntity<>(service.dppGenerator(topic), HttpStatus.OK);
    }
    @Scheduled(cron = "0 0 10 * * *")
    public void scheduled(String topic){
        return ;
    }
}
