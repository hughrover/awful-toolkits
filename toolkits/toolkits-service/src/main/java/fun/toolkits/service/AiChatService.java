package fun.toolkits.service;

import fun.toolkits.model.entity.ChatMessage;
import reactor.core.publisher.Flux;

import java.util.List;

public interface AiChatService {

    /**
     * 流式对话
     * @param sessionId 会话ID
     * @param message 用户消息
     * @return 响应流
     */
    Flux<String> streamChat(Long sessionId, String message);

    /**
     * 获取历史消息
     * @param sessionId 会话ID
     * @return 消息列表
     */
    List<ChatMessage> getHistory(Long sessionId);
}
