package org.example.aipoweredstudyresourcegenerator.controller;

import org.example.aipoweredstudyresourcegenerator.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoteController {
    @Autowired
    private NoteService service;

    @GetMapping("/note")
    public ResponseEntity<String> createNote(@RequestParam String topic){
        return service.getNote(topic);
    }
}
