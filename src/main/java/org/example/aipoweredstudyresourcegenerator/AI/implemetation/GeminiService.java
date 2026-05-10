package org.example.aipoweredstudyresourcegenerator.AI.implemetation;

import org.example.aipoweredstudyresourcegenerator.AI.service.AIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Primary
public class GeminiService implements AIService {
    @Value("${gemini.api.key}")
    private String apikey;
    private final RestTemplate restTemplate=new  RestTemplate();

    public String generated(String prompt){
        String GEMINI_URL =
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent?key="+apikey;
        Map<String,Object > textWrapper=new HashMap<>();
        textWrapper.put("text",prompt);

        Map<String,Object> partWrapper=new HashMap<>();
        partWrapper.put("part",List.of(textWrapper));

        Map<String,Object> body=new HashMap<>();
        body.put("body", List.of(partWrapper));

        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String,Object>> requested=new HttpEntity<>(body,headers);


        ResponseEntity<Map> response=restTemplate.postForEntity(GEMINI_URL,requested,Map.class);
        List candidates = (List) response.getBody().get("candidates");
        Map candidate = (Map) candidates.get(0);

        Map content = (Map) candidate.get("content");
        List parts = (List) content.get("parts");

        Map part = (Map) parts.get(0);

        return part.get("text").toString();
    }
}
