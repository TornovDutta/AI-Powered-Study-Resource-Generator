package org.example.aipoweredstudyresourcegenerator.DAO;

import org.example.aipoweredstudyresourcegenerator.Model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepo extends JpaRepository<Topic,Integer> {
}
