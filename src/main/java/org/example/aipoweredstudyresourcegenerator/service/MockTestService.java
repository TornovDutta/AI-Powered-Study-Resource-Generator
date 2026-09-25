package org.example.aipoweredstudyresourcegenerator.service;

import org.example.aipoweredstudyresourcegenerator.Repo.QuestionRepo;
import org.example.aipoweredstudyresourcegenerator.Repo.TopicRepo;
import org.example.aipoweredstudyresourcegenerator.Model.Questions;
import org.example.aipoweredstudyresourcegenerator.Model.QuestionsWrapper;
import org.example.aipoweredstudyresourcegenerator.Model.Topic;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

public interface MockTestService {
    List<Questions> testGenerator(String topicName);
    void sendMail(String topic, List<Questions> questionsList);
}
