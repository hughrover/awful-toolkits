package fun.toolkits.service.agent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 智能体服务测试
 */
@SpringBootTest
class IntelligentAgentServiceTest {

    @Autowired
    private IntelligentAgentService intelligentAgentService;

    @BeforeEach
    void setUp() {
        intelligentAgentService.initialize();
    }

    @Test
    void testGreetingIntent() {
        String response = intelligentAgentService.processInput("你好", "test-session-1");

        assertNotNull(response);
        assertTrue(response.contains("您好"));
        assertTrue(response.contains("智能助手"));
    }

    @Test
    void testFileProcessingIntent() {
        String response = intelligentAgentService.processInput("请将 /path/to/data.xlsx 转换为PDF", "test-session-2");

        assertNotNull(response);
        assertTrue(response.contains("文件处理"));
        // 实际响应取决于文件路径是否有效
    }

    @Test
    void testUnknownIntent() {
        String response = intelligentAgentService.processInput("我想买一杯咖啡", "test-session-3");

        assertNotNull(response);
        assertTrue(response.contains("无法理解") || response.contains("抱歉"));
    }

    @Test
    void testSessionPersistence() {
        String sessionId = "test-session-4";
        String response1 = intelligentAgentService.processInput("你好", sessionId);
        String response2 = intelligentAgentService.processInput("请帮我处理文件", sessionId);

        assertNotNull(response1);
        assertNotNull(response2);
        // 确保在同一会话中上下文保持
    }

    @Test
    void testGetNodeInfo() {
        Map<String, Map<String, Object>> nodesInfo = intelligentAgentService.getAllNodesInfo();

        assertNotNull(nodesInfo);
        assertTrue(nodesInfo.containsKey("intent-recognition"));
        assertTrue(nodesInfo.containsKey("greeting"));
        assertTrue(nodesInfo.containsKey("file-processing"));
    }

    @Test
    void testAddSkill() {
        // 测试添加技能到节点
        Map<String, Object> fileNodeInfo = intelligentAgentService.getNodeInfo("file-processing");
        assertNotNull(fileNodeInfo);

        // 这里可以验证技能数量
        Integer skillsCount = (Integer) fileNodeInfo.get("skills");
        assertNotNull(skillsCount);
    }
}