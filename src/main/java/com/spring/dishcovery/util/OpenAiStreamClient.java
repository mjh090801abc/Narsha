package com.spring.dishcovery.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

//OpenAiStreamClient.java (OpenAI API와 직접 통신)
public class OpenAiStreamClient {
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build();
    private final String apiKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OpenAiStreamClient(String apiKey){
        this.apiKey = apiKey;
    }

    /**
     * OpenAI chat completions streaming: posts payload (json) to /v1/chat/completions with "stream": true
     * Returns InputStream of the HTTP response body for line-by-line reading by caller.
     */
    /**
     * OpenAI ChatCompletion API 호출 (스트리밍)
     * stream 모드 : AI가 단어·문장 단위로 잘라 보내줌
     */
    public InputStream streamChatCompletions(String payloadJson) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payloadJson))
                .build();

        HttpResponse<InputStream> resp = client.send(req, HttpResponse.BodyHandlers.ofInputStream());
        if (resp.statusCode() >= 400) {
            // read body for error message
            String err = new String(resp.body().readAllBytes());
            throw new IOException("OpenAI API error: " + resp.statusCode() + " - " + err);
        }
        return resp.body();
    }

    public String buildPayloadFromMessages(List<JsonNode> messages, String model) throws IOException {
        // messages is array of {role, content}
        JsonNode root = objectMapper.createObjectNode();
        ((com.fasterxml.jackson.databind.node.ObjectNode)root).put("model", model);
        ((com.fasterxml.jackson.databind.node.ObjectNode)root).put("stream", true);
        ((com.fasterxml.jackson.databind.node.ObjectNode)root).set("messages", objectMapper.valueToTree(messages));
        return objectMapper.writeValueAsString(root);
    }

    //받은 문자열을 JSON으로 변환
    public JsonNode parseChunk(String line) {
        try {
            return objectMapper.readTree(line);
        } catch (Exception e) {
            return null;
        }
    }
}
