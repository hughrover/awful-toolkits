package fun.toolkits.service.project;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.toolkits.model.entity.Project;

import java.math.BigDecimal;
import java.util.Map;

public interface ProjectService extends IService<Project> {

    /**
     * 获取项目及其预算总览
     */
    Map<String, Object> getProjectWithBudget(Long projectId);

    /**
     * 计算项目总预算
     */
    BigDecimal calculateProjectTotalBudget(Long projectId);
}
