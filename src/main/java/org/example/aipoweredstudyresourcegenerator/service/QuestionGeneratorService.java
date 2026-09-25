package org.example.aipoweredstudyresourcegenerator.service;

import org.example.aipoweredstudyresourcegenerator.Repo.QuestionRepo;
import org.example.aipoweredstudyresourcegenerator.Repo.TopicRepo;
import org.example.aipoweredstudyresourcegenerator.Model.QuestionsWrapper;
import org.example.aipoweredstudyresourcegenerator.Model.Topic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

public interface QuestionGeneratorService {
    ResponseEntity<String> generated(String topicName);
}
