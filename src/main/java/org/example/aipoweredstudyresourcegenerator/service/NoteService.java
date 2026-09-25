package org.example.aipoweredstudyresourcegenerator.service;

import org.example.aipoweredstudyresourcegenerator.Repo.NoteRepo;
import org.example.aipoweredstudyresourcegenerator.Model.Note;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface NoteService {
    ResponseEntity<List<Note>> getNote(String topic);
    ResponseEntity<List<Note>> searchNotes(String query);
}
