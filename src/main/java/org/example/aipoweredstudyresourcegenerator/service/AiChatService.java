package org.example.aipoweredstudyresourcegenerator.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface AiChatService {
    String getResponse(String prompt);
}
