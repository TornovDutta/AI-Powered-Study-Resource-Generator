package org.example.aipoweredstudyresourcegenerator.DAO;

import org.example.aipoweredstudyresourcegenerator.Model.QuestionsWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepo extends JpaRepository<QuestionsWrapper,Integer> {
    List<QuestionsWrapper> findByTopic(String topic);
}
