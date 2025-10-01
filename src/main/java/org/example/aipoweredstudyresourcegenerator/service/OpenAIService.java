package org.example.aipoweredstudyresourcegenerator.service;

import org.reactivestreams.Publisher;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {

    private final ChatClient chatClient;

    public OpenAIService(OpenAiChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    public String getResponse(String prompt){
        return chatClient.prompt().user(prompt).call().content().trim();
    }
}
