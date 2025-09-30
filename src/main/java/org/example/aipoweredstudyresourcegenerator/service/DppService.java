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
public class DppService {


    private final  QuestionGenerater generator;


    private final QuestionRepo questionRepo;


    private final TopicRepo topicRepo;


    private final JavaMailSender mail;

    public DppService(QuestionGenerater generator, QuestionRepo questionRepo, TopicRepo topicRepo, JavaMailSender mail) {
        this.generator = generator;
        this.questionRepo = questionRepo;
        this.topicRepo = topicRepo;
        this.mail = mail;
    }

    public List<Questions> dppGenerator(String topicName) {

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

    public void sendMail(String topic,List<Questions> question) {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo("tornovdutta20@gmail.com");
        message.setFrom("tornovdutta@gmail.com");
        message.setText("Dpp start on "+topic+"/n"+question);
        message.setSubject("Remainder");
        mail.send(message);
    }
}
