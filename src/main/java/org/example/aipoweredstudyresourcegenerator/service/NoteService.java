package org.example.aipoweredstudyresourcegenerator.service;

import org.example.aipoweredstudyresourcegenerator.Repo.NoteRepo;
import org.example.aipoweredstudyresourcegenerator.Model.Note;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class NoteService {

    private final OpenAIService openAi;
    private final NoteRepo repo;
    private final EmbeddingService embeddingService;
    private final PineconeService pineconeService;

    // If Pinecone returns a match above this score, treat it as the same topic
    private static final float SIMILARITY_THRESHOLD = 0.75f;

    public NoteService(OpenAIService openAi, NoteRepo repo,
                       EmbeddingService embeddingService, PineconeService pineconeService) {
        this.openAi = openAi;
        this.repo = repo;
        this.embeddingService = embeddingService;
        this.pineconeService = pineconeService;
    }

    public ResponseEntity<List<Note>> getNote(String topic) {
        // 1. Exact match in PostgreSQL
        if (repo.existsByTopic(topic)) {
            return new ResponseEntity<>(repo.findByTopic(topic), HttpStatus.OK);
        }

        // 2. Semantic match in Pinecone — avoids regenerating nearly identical notes
        List<Float> queryEmbedding = embeddingService.embed(topic);
        List<PineconeService.QueryMatch> matches = pineconeService.query(queryEmbedding, 3);

        Optional<PineconeService.QueryMatch> semanticMatch = matches.stream()
            .filter(m -> "note".equals(m.metadata().get("type")) && m.score() >= SIMILARITY_THRESHOLD)
            .findFirst();

        if (semanticMatch.isPresent()) {
            String matchedTopic = semanticMatch.get().metadata().get("topic");
            List<Note> existing = repo.findByTopic(matchedTopic);
            if (!existing.isEmpty()) {
                return new ResponseEntity<>(existing, HttpStatus.OK);
            }
        }

        // 3. Generate new note, save to PostgreSQL, index in Pinecone
        String prompt = "Write a detailed study note on the topic: " + topic;
        String noteContent = openAi.getResponse(prompt);

        Note newNote = new Note();
        newNote.setTopic(topic);
        newNote.setNote(noteContent);
        Note saved = repo.save(newNote);

        // Embed a summary of the content for richer semantic matching
        String embeddingInput = topic + " " + noteContent.substring(0, Math.min(500, noteContent.length()));
        List<Float> noteEmbedding = embeddingService.embed(embeddingInput);
        pineconeService.upsert(
            "note-" + saved.getId(),
            noteEmbedding,
            Map.of("type", "note", "topic", topic, "noteId", String.valueOf(saved.getId()))
        );

        return new ResponseEntity<>(List.of(saved), HttpStatus.CREATED);
    }

    public ResponseEntity<List<Note>> searchNotes(String query) {
        List<Float> queryEmbedding = embeddingService.embed(query);
        List<PineconeService.QueryMatch> matches = pineconeService.query(queryEmbedding, 5);

        List<Note> results = matches.stream()
            .filter(m -> "note".equals(m.metadata().get("type")))
            .map(m -> repo.findByTopic(m.metadata().get("topic")))
            .flatMap(List::stream)
            .toList();

        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}
