package fun.toolkits.service.agent;

/**
 * 基础技能接口
 */
public interface Skill {

    /**
     * 获取技能名称
     */
    String getSkillName();

    /**
     * 获取技能描述
     */
    String getSkillDescription();

    /**
     * 判断技能是否可用
     */
    default boolean isAvailable() {
        return true;
    }
}