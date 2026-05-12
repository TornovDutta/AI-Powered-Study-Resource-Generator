package org.example.aipoweredstudyresourcegenerator.service;

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
public class EmbeddingService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String EMBEDDING_URL = "https://integrate.api.nvidia.com/v1/embeddings";
    private static final String EMBEDDING_MODEL = "nvidia/nv-embed-v1";

    public List<Float> embed(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = Map.of(
            "input", List.of(text),
            "model", EMBEDDING_MODEL,
            "encoding_format", "float",
            "input_type", "query",
            "truncate", "END"
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
