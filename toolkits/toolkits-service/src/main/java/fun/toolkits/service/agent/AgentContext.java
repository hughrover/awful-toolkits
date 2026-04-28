package fun.toolkits.service.agent;

import java.util.HashMap;
import java.util.Map;

/**
 * 智能体上下文
 */
public class AgentContext {
    private final Map<String, Object> variables = new HashMap<>();
    private final Map<String, Object> sessionData = new HashMap<>();

    public void setVariable(String key, Object value) {
        variables.put(key, value);
    }

    public Object getVariable(String key) {
        return variables.get(key);
    }

    public <T> T getVariable(String key, Class<T> type) {
        Object value = variables.get(key);
        if (value != null && type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }

    public void setSessionData(String key, Object value) {
        sessionData.put(key, value);
    }

    public Object getSessionData(String key) {
        return sessionData.get(key);
    }

    public <T> T getSessionData(String key, Class<T> type) {
        Object value = sessionData.get(key);
        if (value != null && type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }

    public Map<String, Object> getAllVariables() {
        return new HashMap<>(variables);
    }

    public Map<String, Object> getAllSessionData() {
        return new HashMap<>(sessionData);
    }

    public void clearVariables() {
        variables.clear();
    }

    public void clearSessionData() {
        sessionData.clear();
    }
}
