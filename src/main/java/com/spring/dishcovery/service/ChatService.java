package com.spring.dishcovery.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.dishcovery.entity.ChatMessage;
import com.spring.dishcovery.mapper.ChatMapper;
import com.spring.dishcovery.util.OpenAiStreamClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    private final ChatMapper chatMapper;
    private final OpenAiStreamClient openAi;
    private final ObjectMapper om = new ObjectMapper();

    // 모델 이름은 원하시는 것으로 변경하세요
    private final String model = "gpt-4o-mini";

    public ChatService(ChatMapper chatMapper, @Value("${openai.api-key}") String key) {
        this.chatMapper = chatMapper;
        this.openAi = new OpenAiStreamClient(key);
    }

    public List<ChatMessage> getHistory(String userId){
        return chatMapper.findByUserId(userId);
    }

    public ChatMessage saveUserMessage(String userId, String content){
        ChatMessage m = new ChatMessage();
        m.setUserId(userId);
        m.setRole("user");
        m.setContent(content);
        m.setCreatedAt(LocalDateTime.now());
        chatMapper.insert(m);
        return m;
    }

    public ChatMessage saveAssistantMessage(String userId, String content){
        ChatMessage m = new ChatMessage();
        m.setUserId(userId);
        m.setRole("assistant");
        m.setContent(content);
        m.setCreatedAt(LocalDateTime.now());
        chatMapper.insert(m);
        return m;
    }

    /**
     * Build messages JSON (List<JsonNode>) from DB history, including the last user message appended.
     */
    public List<JsonNode> buildMessagesForOpenAi(String userId) {
        List<ChatMessage> hist = chatMapper.findByUserId(userId);
        List<JsonNode> messages = new ArrayList<>();
        for (ChatMessage h : hist) {
            messages.add(om.createObjectNode()
                    .put("role", h.getRole())
                    .put("content", h.getContent()));
        }
        return messages;
    }

    /**
     * Create payload JSON string for OpenAI streaming using history (caller may append system message if desired).
     */
    public String createPayloadForStreaming(List<JsonNode> messages) throws IOException {
        return openAi.buildPayloadFromMessages(messages, model);
    }

    public InputStream streamFromOpenAi(String payload) throws IOException, InterruptedException {
        return openAi.streamChatCompletions(payload);
    }

    public JsonNode parseChunk(String jsonLine){
        return openAi.parseChunk(jsonLine);
    }
}
