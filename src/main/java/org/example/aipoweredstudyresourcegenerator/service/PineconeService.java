package org.example.aipoweredstudyresourcegenerator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PineconeService {

    @Value("${pinecone.api-key}")
    private String apiKey;

    @Value("${pinecone.index-host}")
    private String indexHost;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Api-Key", apiKey);
        return headers;
    }

    public void upsert(String id, List<Float> values, Map<String, Object> metadata) {
        Map<String, Object> vector = new HashMap<>();
        vector.put("id", id);
        vector.put("values", values);
        vector.put("metadata", metadata);

        Map<String, Object> body = Map.of("vectors", List.of(vector));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers());
        restTemplate.exchange(indexHost + "/vectors/upsert", HttpMethod.POST, request, String.class);
    }

    public List<QueryMatch> query(List<Float> values, int topK) {
        Map<String, Object> body = Map.of(
            "vector", values,
            "topK", topK,
            "includeMetadata", true
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers());
        ResponseEntity<String> response = restTemplate.exchange(indexHost + "/query", HttpMethod.POST, request, String.class);

        List<QueryMatch> matches = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode matchesNode = root.get("matches");
            if (matchesNode == null || matchesNode.isNull()) return matches;

            for (JsonNode match : matchesNode) {
                String matchId = match.get("id").asText();
                float score = match.get("score").floatValue();
                Map<String, String> meta = new HashMap<>();
                if (match.has("metadata")) {
                    match.get("metadata").fields().forEachRemaining(e ->
                        meta.put(e.getKey(), e.getValue().asText()));
                }
                matches.add(new QueryMatch(matchId, score, meta));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Pinecone query response", e);
        }
        return matches;
    }

    public record QueryMatch(String id, float score, Map<String, String> metadata) {}
}
