package org.example.aipoweredstudyresourcegenerator.service;

import org.example.aipoweredstudyresourcegenerator.DAO.QuestionRepo;
import org.example.aipoweredstudyresourcegenerator.DAO.TopicRepo;
import org.example.aipoweredstudyresourcegenerator.Model.QuestionsWrapper;
import org.example.aipoweredstudyresourcegenerator.Model.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class QuestionGenerater {
    @Autowired
    private OpenAIService service;
    @Autowired
    private TopicRepo topicRepository;

    @Autowired
    private QuestionRepo questionsWrapperRepository;

    public ResponseEntity<String> generated(String topicName){
        String prompt = "Generate 10 MCQs on the topic: " + topicName + ". Format like this:\n" +
                "Question: ...\nA) ...\nB) ...\nC) ...\nD) ...\nAnswer: ...";

        String response = service.getResponse(prompt);
        Topic topic = new Topic();
        topicRepository.save(topic);

        String[] questionsArray = response.split("Question:");
        for (String qBlock : questionsArray) {
            if (qBlock.trim().isEmpty()) continue;

            String[] lines = qBlock.trim().split("\n");
            QuestionsWrapper q = new QuestionsWrapper();

            q.setQuestion(lines[0].trim()); // The question line

            for (String line : lines) {
                if (line.startsWith("A)")) q.setOptionA(line.substring(2).trim());
                else if (line.startsWith("B)")) q.setOptionB(line.substring(2).trim());
                else if (line.startsWith("C)")) q.setOptionC(line.substring(2).trim());
                else if (line.startsWith("D)")) q.setOptionD(line.substring(2).trim());
                else if (line.startsWith("Answer:")) q.setAnswer(line.substring(7).trim());
            }

            q.setTopic(topic);
            questionsWrapperRepository.save(q);
        }
        return new ResponseEntity<>("Ok", HttpStatus.OK);

    }
}
