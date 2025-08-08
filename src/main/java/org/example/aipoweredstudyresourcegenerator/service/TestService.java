package org.example.aipoweredstudyresourcegenerator.service;

import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.example.aipoweredstudyresourcegenerator.DAO.QuestionRepo;
import org.example.aipoweredstudyresourcegenerator.Model.Questions;
import org.example.aipoweredstudyresourcegenerator.Model.QuestionsWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {
    @Autowired
    private QuestionGenerater generator;
    @Autowired
    private QuestionRepo questionRepo;

    public List<Questions> testGenerator(String topic){
        generator.generated(topic);
        List<QuestionsWrapper> questionsWrapper=questionRepo.findByTopic(topic);
        List<Questions> questions= new ArrayList<>();
        for(QuestionsWrapper question:questionsWrapper){
            Questions q=new Questions();
            q.setId(question.getId());
            q.setQuestion(question.getQuestion());
            questions.add(q);
        }
        return questions;
    }
}
