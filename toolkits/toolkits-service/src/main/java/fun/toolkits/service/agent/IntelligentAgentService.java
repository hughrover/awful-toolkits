package fun.toolkits.service.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 智能体服务 - 管理意图路由和节点调度
 */
@Service
public class IntelligentAgentService {

    @Autowired
    private IntentRecognitionNode intentRecognitionNode;

    @Autowired
    private GreetingAgentNode greetingAgentNode;

    @Autowired
    private FileProcessingAgentNode fileProcessingAgentNode;

    private final Map<String, AgentNode> nodeRegistry = new ConcurrentHashMap<>();
    private final Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 初始化智能体
     */
    public void initialize() {
        // 注册节点
        nodeRegistry.put("intent-recognition", intentRecognitionNode);
        nodeRegistry.put("greeting", greetingAgentNode);
        nodeRegistry.put("file-processing", fileProcessingAgentNode);
    }

    /**
     * 处理用户输入
     */
    public String processInput(String input, String sessionId) {
        // 获取或创建会话上下文
        AgentContext context = getSessionContext(sessionId);

        try {
            // 第一步：意图识别
            String intent = intentRecognitionNode.process(input, context);
            context.setVariable("intent", intent);

            // 第二步：根据意图路由到相应节点
            AgentNode targetNode = getTargetNode(intent);
            if (targetNode != null) {
                return targetNode.process(input, context);
            } else {
                return "抱歉，我暂时无法理解您的意图。请尝试其他表达方式。";
            }
        } catch (Exception e) {
            return "处理过程中发生错误：" + e.getMessage();
        }
    }

    /**
     * 根据意图获取目标节点
     */
    private AgentNode getTargetNode(String intent) {
        switch (intent) {
            case "GREETING":
                return greetingAgentNode;
            case "FILE_PROCESSING":
                return fileProcessingAgentNode;
            case "USER_MANAGEMENT":
                // TODO: 实现用户管理节点
                return null;
            case "HELP":
                // TODO: 实现帮助节点
                return null;
            case "EXIT":
                return null;
            default:
                return null;
        }
    }

    /**
     * 获取会话上下文
     */
    private AgentContext getSessionContext(String sessionId) {
        AgentContext context = (AgentContext) sessionStore.get(sessionId);
        if (context == null) {
            context = new AgentContext();
            context.setVariable("sessionId", sessionId);
            sessionStore.put(sessionId, context);
        }
        return context;
    }

    /**
     * 添加技能到节点
     */
    public void addSkillToNode(String nodeName, Skill skill) {
        AgentNode node = nodeRegistry.get(nodeName);
        if (node != null) {
            node.addSkill(skill);
        }
    }

    /**
     * 获取节点信息
     */
    public Map<String, Object> getNodeInfo(String nodeName) {
        AgentNode node = nodeRegistry.get(nodeName);
        if (node != null) {
            Map<String, Object> info = new HashMap<>();
            info.put("name", node.getNodeName());
            info.put("description", node.getNodeDescription());
            info.put("skills", node.getSkills().size());
            return info;
        }
        return null;
    }

    /**
     * 获取所有节点信息
     */
    public Map<String, Map<String, Object>> getAllNodesInfo() {
        Map<String, Map<String, Object>> allInfo = new HashMap<>();
        nodeRegistry.forEach((name, node) -> {
            allInfo.put(name, getNodeInfo(name));
        });
        return allInfo;
    }
}
