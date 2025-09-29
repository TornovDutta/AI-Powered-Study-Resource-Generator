package org.example.aipoweredstudyresourcegenerator.controller;

import org.example.aipoweredstudyresourcegenerator.Model.Note;
import org.example.aipoweredstudyresourcegenerator.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("note")
public class NoteController {

    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    @PostMapping("")
    public ResponseEntity<List <Note>> createNote(@RequestParam String topic){
        return service.getNote(topic);
    }


}
