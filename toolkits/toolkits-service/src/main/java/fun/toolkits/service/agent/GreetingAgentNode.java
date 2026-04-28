package fun.toolkits.service.agent;

import org.springframework.stereotype.Component;

/**
 * 问候节点
 */
@Component
public class GreetingAgentNode extends AbstractAgentNode {

    public GreetingAgentNode() {
        super("greeting", "处理用户问候的节点");
    }

    @Override
    public String process(String input, AgentContext context) {
        return "您好！我是智能助手，很高兴为您服务。我可以帮助您完成Excel转PDF、用户管理等功能。请告诉我您需要什么帮助？";
    }
}