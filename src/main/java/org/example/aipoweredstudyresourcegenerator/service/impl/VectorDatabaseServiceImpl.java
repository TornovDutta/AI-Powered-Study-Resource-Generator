package org.example.aipoweredstudyresourcegenerator.service.impl;
import org.example.aipoweredstudyresourcegenerator.service.*;

import org.example.aipoweredstudyresourcegenerator.service.VectorDatabaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VectorDatabaseServiceImpl implements VectorDatabaseService {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public VectorDatabaseServiceImpl(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
        initTable();
    }

    private void initTable() {
        jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS vector;");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS vector_store (" +
                "id VARCHAR(255) PRIMARY KEY, " +
                "embedding vector(384), " +
                "metadata JSONB);");
    }

    public void upsert(String id, List<Float> values, Map<String, Object> metadata) {
        String vectorStr = "[" + values.stream().map(String::valueOf).collect(Collectors.joining(",")) + "]";
        String metadataStr;
        try {
            metadataStr = objectMapper.writeValueAsString(metadata);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize metadata", e);
        }

        String sql = "INSERT INTO vector_store (id, embedding, metadata) VALUES (?, ?::vector, ?::jsonb) " +
                     "ON CONFLICT (id) DO UPDATE SET embedding = EXCLUDED.embedding, metadata = EXCLUDED.metadata";
        jdbcTemplate.update(sql, id, vectorStr, metadataStr);
    }

    public List<QueryMatch> query(List<Float> values, int topK) {
        String vectorStr = "[" + values.stream().map(String::valueOf).collect(Collectors.joining(",")) + "]";
        
        String sql = "SELECT id, metadata, 1 - (embedding <=> ?::vector) as score " +
                     "FROM vector_store ORDER BY embedding <=> ?::vector LIMIT ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            String id = rs.getString("id");
            float score = rs.getFloat("score");
            String metaStr = rs.getString("metadata");
            Map<String, String> meta = new HashMap<>();
            if (metaStr != null) {
                try {
                    meta = objectMapper.readValue(metaStr, new TypeReference<Map<String, String>>() {});
                } catch (Exception e) {
                    throw new RuntimeException("Failed to parse metadata", e);
                }
            }
            return new QueryMatch(id, score, meta);
        }, vectorStr, vectorStr, topK);
    }
}

