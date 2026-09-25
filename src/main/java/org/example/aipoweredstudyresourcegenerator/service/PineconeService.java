package org.example.aipoweredstudyresourcegenerator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

public interface PineconeService {
    void upsert(String id, List<Float> values, Map<String, Object> metadata);
    List<QueryMatch> query(List<Float> values, int topK);
    public record QueryMatch(String id, float score, Map<String, String> metadata) {}
}
