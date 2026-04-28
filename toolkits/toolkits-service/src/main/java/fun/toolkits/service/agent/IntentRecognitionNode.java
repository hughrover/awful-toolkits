package fun.toolkits.service.agent;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 意图识别节点
 */
@Component
public class IntentRecognitionNode implements AgentNode {

    @Override
    public String getNodeName() {
        return "intent-recognition";
    }

    @Override
    public String getNodeDescription() {
        return "识别用户输入的意图，路由到相应的处理节点";
    }

    @Override
    public String process(String input, AgentContext context) {
        // 如果已经识别过意图，直接返回
        String cachedIntent = context.getVariable("intent", String.class);
        if (cachedIntent != null) {
            return cachedIntent;
        }

        // 使用关键词进行意图识别
        Intent intent = Intent.fromText(input);

        // 将意图保存到上下文
        context.setVariable("intent", intent.name());

        return intent.name();
    }

    @Override
    public void addChildNode(AgentNode childNode) {
    }

    @Override
    public List<AgentNode> getChildNodes() {
        return null;
    }

    @Override
    public void setParentNode(AgentNode parentNode) {
    }

    @Override
    public AgentNode getParentNode() {
        return null;
    }

    @Override
    public void addSkill(Skill skill) {
    }

    @Override
    public List<Skill> getSkills() {
        return null;
    }
}
