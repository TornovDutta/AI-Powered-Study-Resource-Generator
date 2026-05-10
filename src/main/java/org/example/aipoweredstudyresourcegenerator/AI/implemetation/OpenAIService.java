package org.example.aipoweredstudyresourcegenerator.AI.implemetation;

import org.example.aipoweredstudyresourcegenerator.AI.service.AIService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService implements AIService {

    private final ChatClient chatClient;

    public OpenAIService(OpenAiChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }
    public String generated(String prompt){
        String response=chatClient.prompt(prompt).call().content();
        return response;
    }
}
