package org.example.aipoweredstudyresourcegenerator.service.impl;
import org.example.aipoweredstudyresourcegenerator.service.*;

import org.example.aipoweredstudyresourcegenerator.service.EmbeddingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EmbeddingServiceImpl implements EmbeddingService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public EmbeddingServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    private static final String EMBEDDING_URL = "https://api-inference.huggingface.co/v1/embeddings";
    private static final String EMBEDDING_MODEL = "sentence-transformers/all-MiniLM-L6-v2";

    public List<Float> embed(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = Map.of(
            "input", text,
            "model", EMBEDDING_MODEL
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(EMBEDDING_URL, HttpMethod.POST, request, String.class);

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode embeddingArray = root.get("data").get(0).get("embedding");
            List<Float> embedding = new ArrayList<>();
            for (JsonNode val : embeddingArray) {
                embedding.add(val.floatValue());
            }
            return embedding;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse embedding response", e);
        }
    }
}

