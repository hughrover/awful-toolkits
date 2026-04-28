package fun.toolkits.service.agent;

import java.util.List;
import java.util.Map;

/**
 * 智能体节点接口
 */
public interface AgentNode {

    /**
     * 获取节点名称
     */
    String getNodeName();

    /**
     * 获取节点描述
     */
    String getNodeDescription();

    /**
     * 处理输入
     * @param input 输入内容
     * @param context 上下文
     * @return 处理结果
     */
    String process(String input, AgentContext context);

    /**
     * 添加子节点
     * @param childNode 子节点
     */
    void addChildNode(AgentNode childNode);

    /**
     * 获取子节点列表
     */
    List<AgentNode> getChildNodes();

    /**
     * 设置父节点
     * @param parentNode 父节点
     */
    void setParentNode(AgentNode parentNode);

    /**
     * 获取父节点
     */
    AgentNode getParentNode();

    /**
     * 添加技能
     * @param skill 技能
     */
    void addSkill(Skill skill);

    /**
     * 获取所有技能
     */
    List<Skill> getSkills();
}