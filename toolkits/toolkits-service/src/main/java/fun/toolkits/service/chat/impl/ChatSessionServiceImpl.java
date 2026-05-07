package fun.toolkits.service.chat.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.toolkits.dal.mapper.ChatSessionMapper;
import fun.toolkits.model.entity.ChatSession;
import fun.toolkits.service.chat.ChatSessionService;
import org.springframework.stereotype.Service;

@Service
public class ChatSessionServiceImpl extends ServiceImpl<ChatSessionMapper, ChatSession> implements ChatSessionService {
}
