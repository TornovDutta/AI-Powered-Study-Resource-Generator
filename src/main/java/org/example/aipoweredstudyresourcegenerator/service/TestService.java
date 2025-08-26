package org.example.aipoweredstudyresourcegenerator.service;

import org.example.aipoweredstudyresourcegenerator.DAO.QuestionRepo;
import org.example.aipoweredstudyresourcegenerator.DAO.TopicRepo;
import org.example.aipoweredstudyresourcegenerator.Model.Questions;
import org.example.aipoweredstudyresourcegenerator.Model.QuestionsWrapper;
import org.example.aipoweredstudyresourcegenerator.Model.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {

    @Autowired
    private QuestionGenerater generator;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private TopicRepo topicRepo;

    @Autowired
    private JavaMailSender mail;


    public List<Questions> testGenerator(String topicName) {

        if(!topicRepo.existsByName(topicName)){
            generator.generated(topicName);
        }



        Topic topicEntity = topicRepo.findByName(topicName)
                .orElseThrow(() -> new RuntimeException("Topic not found: " + topicName));


        List<QuestionsWrapper> questionsWrapperList = questionRepo.findByTopic(topicEntity);


        List<Questions> questionsList = new ArrayList<>();
        for (QuestionsWrapper wrapper : questionsWrapperList) {
            Questions q = new Questions();
            q.setId(wrapper.getId());
            q.setQuestion(wrapper.getQuestion());
            questionsList.add(q);
        }

        return questionsList;
    }
    public void sendMail(String topic) {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo("tornovdutta20@gmail.com");
        message.setFrom("tornovdutta@gmail.com");
        message.setText("Dpp start on "+topic);
        message.setSubject("Remainder");
        mail.send(message);
    }
}
