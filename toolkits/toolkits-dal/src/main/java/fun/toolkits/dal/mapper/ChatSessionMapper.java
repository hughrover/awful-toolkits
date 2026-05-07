package fun.toolkits.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.toolkits.model.entity.ChatSession;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatSessionMapper extends BaseMapper<ChatSession> {
}
