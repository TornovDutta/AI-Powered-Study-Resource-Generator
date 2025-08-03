package org.example.aipoweredstudyresourcegenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
    @Autowired
    private OpenAIService openAi;

    public ResponseEntity<String> getNote(String topic) {
        String prompt = "Write a detailed study note on the topic: " + topic + ". Do not include any introductory or " +
                "closing statements—only the note content.";

        String note=openAi.getResponse(prompt);


        return new ResponseEntity<>(note, HttpStatus.OK);

    }
}
