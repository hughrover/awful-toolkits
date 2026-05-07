package fun.toolkits.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import fun.toolkits.dal.mapper.ChatMessageMapper;
import fun.toolkits.model.entity.ChatMessage;
import fun.toolkits.service.AiChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class AiChatServiceImpl implements AiChatService {

    private final ChatClient chatClient;

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    public AiChatServiceImpl(ChatClient.Builder builder) {
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)
                .build();

        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Override
    public Flux<String> streamChat(Long sessionId, String message) {
        // 保存用户消息
        saveMessage(sessionId, "user", message);

        StringBuilder fullResponse = new StringBuilder();

        return chatClient.prompt()
                .user(message)
                .toolNames("translateText", "generateImage")
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId))
                .stream()
                .content()
                .doOnNext(fullResponse::append)
                .doOnComplete(() -> {
                    // 流式完成后保存完整消息
                    saveMessage(sessionId, "assistant", fullResponse.toString());
                });
    }

    @Override
    public List<ChatMessage> getHistory(Long sessionId) {
        return chatMessageMapper.selectList(
                new LambdaQueryWrapper<ChatMessage>()
                        .eq(ChatMessage::getSessionId, sessionId)
                        .orderByAsc(ChatMessage::getCreatedAt)
        );
    }

    private void saveMessage(Long sessionId, String role, String content) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSessionId(sessionId);
        chatMessage.setRole(role);
        chatMessage.setContent(content);
        chatMessage.setCreatedAt(System.currentTimeMillis());
        chatMessageMapper.insert(chatMessage);
    }
}
