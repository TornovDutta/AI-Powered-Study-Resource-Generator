package org.example.aipoweredstudyresourcegenerator.service;

import org.example.aipoweredstudyresourcegenerator.DAO.NoteRepo;
import org.example.aipoweredstudyresourcegenerator.Model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    @Autowired
    private OpenAIService openAi;

    @Autowired
    private NoteRepo repo;

    public ResponseEntity<List<Note>> getNote(String topic) {
        boolean exists = repo.existsByTopic(topic);
        if (!exists) {
            String prompt = "Write a detailed study note on the topic: " + topic;
            String note = openAi.getResponse(prompt);
            Note newNote = new Note();
            newNote.setTopic(topic);
            newNote.setNote(note);
            repo.save(newNote);
        }
        List<Note> result = repo.findByTopic(topic);
        return new ResponseEntity<>(result, exists ? HttpStatus.OK : HttpStatus.CREATED);
    }

}
