package org.example.aipoweredstudyresourcegenerator.DAO;

import org.example.aipoweredstudyresourcegenerator.Model.QuestionsWrapper;
import org.example.aipoweredstudyresourcegenerator.Model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepo extends JpaRepository<QuestionsWrapper,Integer> {

    List<QuestionsWrapper> findByTopic(Topic topic);
}
