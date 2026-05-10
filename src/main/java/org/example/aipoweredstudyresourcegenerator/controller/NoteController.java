package org.example.aipoweredstudyresourcegenerator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.aipoweredstudyresourcegenerator.Model.Note;
import org.example.aipoweredstudyresourcegenerator.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("note")
@Tag(name = "Notes", description = "Generate and retrieve AI-powered study notes")
public class NoteController {

    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    @GetMapping("")
    @Operation(summary = "Get notes for a topic", description = "Returns existing notes or generates new ones via NVIDIA AI")
    public ResponseEntity<List<Note>> createNote(@RequestParam String topic) {
        return service.getNote(topic);
    }
}
