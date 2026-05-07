package fun.toolkits.service;

import fun.toolkits.dal.mapper.ChatMessageMapper;
import fun.toolkits.service.impl.AiChatServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ai.chat.client.ChatClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AiChatServiceTest {

    @Mock
    private ChatClient.Builder chatClientBuilder;

    @Mock
    private ChatMessageMapper chatMessageMapper;

    private AiChatServiceImpl aiChatService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ChatClient chatClient = mock(ChatClient.class);
        when(chatClientBuilder.defaultAdvisors((org.springframework.ai.chat.client.advisor.api.Advisor[]) any())).thenReturn(chatClientBuilder);
        when(chatClientBuilder.build()).thenReturn(chatClient);
        
        aiChatService = new AiChatServiceImpl(chatClientBuilder);
    }

    @Test
    public void testGetHistory() {
        // Simple test to verify the service is injectable and can call mapper
        aiChatService.getHistory(1L);
    }
}
