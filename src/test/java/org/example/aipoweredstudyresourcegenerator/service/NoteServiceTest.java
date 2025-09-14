package org.example.aipoweredstudyresourcegenerator.service;
import org.example.aipoweredstudyresourcegenerator.DAO.NoteRepo;
import org.example.aipoweredstudyresourcegenerator.Model.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoteServiceTest {

    @Mock
    private OpenAIService openAi;

    @Mock
    private NoteRepo repo;

    @InjectMocks
    private NoteService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetNote_NewTopic_ShouldGenerateAndSave() {
        String topic = "Java";
        String generatedNote = "Java is a programming language";

        when(repo.existsByTopic(topic)).thenReturn(false);
        when(openAi.getResponse(anyString())).thenReturn(generatedNote);
        when(repo.findByTopic(topic)).thenReturn(List.of(new Note(1, topic, generatedNote)));

        ResponseEntity<List<Note>> response = service.getNote(topic);

        assertEquals(201, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
        assertEquals(generatedNote, response.getBody().get(0).getNote());

        verify(repo, times(1)).save(any(Note.class));
        System.out.println("Ok");
    }

    @Test
    void testGetNote_ExistingTopic_ShouldReturnFromRepo() {
        String topic = "Spring Boot";
        String existingNote = "Spring Boot simplifies Java development";

        when(repo.existsByTopic(topic)).thenReturn(true);
        when(repo.findByTopic(topic)).thenReturn(List.of(new Note(2, topic, existingNote)));

        ResponseEntity<List<Note>> response = service.getNote(topic);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(existingNote, response.getBody().get(0).getNote());

        verify(openAi, never()).getResponse(anyString());
        verify(repo, never()).save(any(Note.class));
        System.out.println("Ok");
    }
}
