package org.example.aipoweredstudyresourcegenerator.DAO;

import org.example.aipoweredstudyresourcegenerator.Model.QuestionsWrapper;
import org.example.aipoweredstudyresourcegenerator.Model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;


@Repository
public interface TopicRepo extends JpaRepository<Topic,Integer> {

    Optional<Topic> findByName(String name);


}
