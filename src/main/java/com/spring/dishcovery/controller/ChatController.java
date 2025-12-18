package com.spring.dishcovery.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.dishcovery.entity.ChatMessage;
import com.spring.dishcovery.config.CookieUtil;
import com.spring.dishcovery.config.JwtUtil;
import com.spring.dishcovery.service.ChatService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/*****
 ***       | 기능                  | 설명                             |
 ***       | ------------------- | ------------------------------ |
 ***       | `/api/chat/history` | 기존 채팅 가져오기                         |
 ***       | `/api/chat/send`    | 사용자가 메시지 보낼 때 호출                 |
 ***       | `/api/chat/stream`  | AI가 조각 단위로 답변을 보냄(stream, SSE)   |
 ***       | `SseEmitter`        | 프론트에 실시간 스트림 전달                  |
 ***       | 전체 메시지 완성되면     | DB에 저장                               |
 ***/

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ObjectMapper om = new ObjectMapper();
    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;

    // 페이지
    @GetMapping("/chat")
    public String chatPage(Model model, HttpServletRequest request) {
        // 실제 앱이면 로그인한 userId를 세션/JWT에서 넣으세요

        String userId ="";
        String token = cookieUtil.getTokenFromCookies(request, "JWT_TOKEN");
        if(token != null) {
            userId = jwtUtil.getUserIdFromToken(token);
        }


        model.addAttribute("userId", userId);
        return "recipe/chat";
    }

    // 히스토리 API
    @GetMapping("/api/chat/history")
    @ResponseBody
    public List<ChatMessage> history(@RequestParam String userId){

        return chatService.getHistory(userId);
    }

    // 사용자 메시지 저장 (클라이언트가 먼저 호출)
    @PostMapping("/api/chat/send")
    @ResponseBody
    public Map<String,Object> send(@RequestBody Map<String,Object> body){
        String userId = body.get("userId").toString();
        String message = body.get("message").toString();

        ChatMessage saved = chatService.saveUserMessage(userId, message);
        return Map.of("messageId", saved.getId());
    }

    // SSE 스트리밍 엔드포인트: OpenAI 스트림을 읽어 조각별로 클라이언트에 보냄
    // OpenAI의 스트리밍 응답을 받아서 SSE(Server-Sent Event) 방식으로 프론트에 실시간 전송하는 메소드.
    @GetMapping(path = "/api/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@RequestParam String userId) {
        //클라이언트가 userId를 넘기면 그 사용자의 대화 스트리밍을 제공.
        //sse연결 유지 객체생성 / 0L은 타임아웃 없음, 클라이언트가 끊을때까지 제공
        SseEmitter emitter = new SseEmitter(0L); // no timeout
        Executors.newSingleThreadExecutor().submit(() -> {
            //스트리밍으로 오는 GPT 응답 조각(토큰)을 모아서 최종 메시지를 저장하는 용도.
            StringBuilder assistantFull = new StringBuilder();
            try {
                // build messages array (history),DB에 저장된 이전 대화 기록을 불러와 OpenAI 메시지 형식으로 조립.
                List<JsonNode> messages = chatService.buildMessagesForOpenAi(userId);
                // system 프롬프트를 첫반째 메시지로 삽입
                JsonNode systemNode = om.createObjectNode().put("role","system")
                        .put("content","당신은 친절한 요리 어시스턴트입니다. 레시피를 추천하고, 재료와 조리법 요약을 제공하세요.");
                messages.add(0, systemNode);
                //OpenAI에게 스트리밍 요청할 JSON payload 생성
                //해당 payload로 OpenAI Stream API 호출 → InputStream 형태로 받음
                String payload = chatService.createPayloadForStreaming(messages);
                InputStream is = chatService.streamFromOpenAi(payload);
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.isBlank()) continue;
                        // OpenAI streaming lines are like: data: { ... } or data: [DONE]
                        if (line.startsWith("data: ")) line = line.substring(6);

                        //OpenAI가 스트림 종료 신호 → SSE event("end") 프론트로 전송 후 루프 종료.
                        if ("[DONE]".equals(line.trim())) {
                            emitter.send(SseEmitter.event().name("end").data("[DONE]"));
                            break;
                        }
                        // parse JSON chunk
                        JsonNode chunk = chatService.parseChunk(line.trim());
                        if (chunk == null) continue;
                        JsonNode choices = chunk.path("choices");
                        //OpenAI 스트리밍 형식에 맞춰 content 부분 추출.
                        if (choices.isArray() && choices.size() > 0) {
                            JsonNode delta = choices.get(0).path("delta");
                            JsonNode content = delta.path("content");
                           //추출된 텍스트 조각을 저장 + SSE로 프론트에 즉시 전송
                            if (!content.isMissingNode()) {
                                String part = content.asText();
                                assistantFull.append(part);
                                //emitter.send(SseEmitter.event().name("message").data(part));
                                emitter.send(SseEmitter.event().data(part));
                            }
                        }
                    }
                }

                // 스트리밍이 끝나면 누적된 최종 답변 전체를 DB에 저장.
                chatService.saveAssistantMessage(userId, assistantFull.toString());
                //sse 종료
                emitter.complete();
            } catch (Exception e) {
                try { emitter.send(SseEmitter.event().name("error").data(e.getMessage())); } catch (Exception ignored){}
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }
}
