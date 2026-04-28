package fun.toolkits.service.agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 抽象智能体节点，提供基础实现
 */
public abstract class AbstractAgentNode implements AgentNode {

    protected String nodeName;
    protected String nodeDescription;
    protected AgentNode parentNode;
    protected final List<AgentNode> childNodes = new ArrayList<>();
    protected final List<Skill> skills = new ArrayList<>();

    public AbstractAgentNode(String nodeName, String nodeDescription) {
        this.nodeName = nodeName;
        this.nodeDescription = nodeDescription;
    }

    @Override
    public String getNodeName() {
        return nodeName;
    }

    @Override
    public String getNodeDescription() {
        return nodeDescription;
    }

    @Override
    public void addChildNode(AgentNode childNode) {
        if (childNode != null) {
            childNodes.add(childNode);
            childNode.setParentNode(this);
        }
    }

    @Override
    public List<AgentNode> getChildNodes() {
        return Collections.unmodifiableList(childNodes);
    }

    @Override
    public void setParentNode(AgentNode parentNode) {
        this.parentNode = parentNode;
    }

    @Override
    public AgentNode getParentNode() {
        return parentNode;
    }

    @Override
    public void addSkill(Skill skill) {
        if (skill != null) {
            skills.add(skill);
        }
    }

    @Override
    public List<Skill> getSkills() {
        return Collections.unmodifiableList(skills);
    }
}