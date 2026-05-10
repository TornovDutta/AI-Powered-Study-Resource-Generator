package org.example.aipoweredstudyresourcegenerator.AI.implemetation;

import org.example.aipoweredstudyresourcegenerator.AI.service.AIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Primary
public class NvidiaService implements AIService {

    @Value("${NVIDIA_API_KEY}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String NVIDIA_URL = "https://integrate.api.nvidia.com/v1/chat/completions";
    private static final String MODEL = "meta/llama-3.1-8b-instruct";

    public String generated(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> message = Map.of("role", "user", "content", prompt);
        Map<String, Object> body = Map.of(
                "model", MODEL,
                "messages", List.of(message),
                "temperature", 0.5,
                "max_tokens", 1024
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(NVIDIA_URL, request, Map.class);

        List choices = (List) response.getBody().get("choices");
        Map choice = (Map) choices.get(0);
        Map messageResponse = (Map) choice.get("message");
        return messageResponse.get("content").toString();
    }
}
