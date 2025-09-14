package org.example.aipoweredstudyresourcegenerator.service;

import org.example.aipoweredstudyresourcegenerator.DAO.QuestionRepo;
import org.example.aipoweredstudyresourcegenerator.DAO.TopicRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
class QuestionGeneraterTest {
    @Mock
    private OpenAIService service;
    @Mock
    private TopicRepo topicRepository;

    @Mock
    private QuestionRepo questionsWrapperRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generated() {
        String input="java";
        String output="ok";


    }
}