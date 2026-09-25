package org.example.aipoweredstudyresourcegenerator.service.impl;
import org.example.aipoweredstudyresourcegenerator.service.*;

import org.example.aipoweredstudyresourcegenerator.service.AiChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AiChatServiceImpl implements AiChatService {

    private final ChatClient chatClient;

    public AiChatServiceImpl(OpenAiChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    public String getResponse(String prompt){
        return chatClient.prompt().user(prompt).call().content().trim();
    }
}

