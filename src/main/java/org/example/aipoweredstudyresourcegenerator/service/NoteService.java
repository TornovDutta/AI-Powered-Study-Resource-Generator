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
        String note="";
        if(!repo.existsByTopic(topic)){
            String prompt = "Write a detailed study note on the topic: " + topic + ". Do not include any introductory or " +
                    "closing statements—only the note content.";

            note=openAi.getResponse(prompt);
            Note newnote=new Note();
            newnote.setTopic(topic);
            newnote.setNote(note);
            repo.save(newnote);
        }
        List<Note> result=repo.findByTopic(topic);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }
}
