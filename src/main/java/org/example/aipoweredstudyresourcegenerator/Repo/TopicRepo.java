package org.example.aipoweredstudyresourcegenerator.Repo;

import org.example.aipoweredstudyresourcegenerator.Model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;


@Repository
public interface TopicRepo extends JpaRepository<Topic,Integer> {

    Optional<Topic> findByName(String name);


    boolean existsByName(String name);
}
