package org.example.aipoweredstudyresourcegenerator.DAO;

import org.example.aipoweredstudyresourcegenerator.Model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepo extends JpaRepository<Note,Integer> {
    boolean existsByTopic(String topic);

    List<Note> findByTopic(String topic);
}
