package fun.toolkits.web.controller;

import fun.toolkits.model.common.ApiResult;
import fun.toolkits.model.entity.ChatSession;
import fun.toolkits.service.chat.ChatSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat/sessions")
@RequiredArgsConstructor
public class ChatSessionController {

    private final ChatSessionService chatSessionService;

    @GetMapping
    public ApiResult<List<ChatSession>> list() {
        return ApiResult.ok(chatSessionService.list());
    }

    @PostMapping
    public ApiResult<ChatSession> create(@RequestBody ChatSession session) {
        long now = System.currentTimeMillis();
        session.setCreatedAt(now);
        session.setUpdatedAt(now);
        if (session.getUserId() == null) {
            session.setUserId(1L); // Default user for MVP
        }
        chatSessionService.save(session);
        return ApiResult.ok(session);
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        chatSessionService.removeById(id);
        return ApiResult.ok();
    }
}
