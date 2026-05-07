package fun.toolkits.web.controller;

import fun.toolkits.service.AiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final AiChatService aiChatService;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestParam Long sessionId, @RequestParam String message) {
        return aiChatService.streamChat(sessionId, message);
    }

    @GetMapping("/history/{sessionId}")
    public fun.toolkits.model.common.ApiResult<java.util.List<fun.toolkits.model.entity.ChatMessage>> getHistory(@PathVariable Long sessionId) {
        return fun.toolkits.model.common.ApiResult.ok(aiChatService.getHistory(sessionId));
    }
}
