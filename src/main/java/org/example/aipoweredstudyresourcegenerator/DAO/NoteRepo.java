package org.example.aipoweredstudyresourcegenerator.DAO;

import org.example.aipoweredstudyresourcegenerator.Model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepo extends JpaRepository<Note,Integer> {
}
