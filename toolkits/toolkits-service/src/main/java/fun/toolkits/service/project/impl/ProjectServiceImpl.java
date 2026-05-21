package fun.toolkits.service.project.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.toolkits.dal.mapper.BudgetItemMapper;
import fun.toolkits.dal.mapper.FeatureBreakdownMapper;
import fun.toolkits.dal.mapper.PersonnelProjectMapper;
import fun.toolkits.dal.mapper.ProjectMapper;
import fun.toolkits.model.entity.BudgetItem;
import fun.toolkits.model.entity.FeatureBreakdown;
import fun.toolkits.model.entity.PersonnelProject;
import fun.toolkits.model.entity.Project;
import fun.toolkits.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    private final FeatureBreakdownMapper featureBreakdownMapper;
    private final BudgetItemMapper budgetItemMapper;
    private final PersonnelProjectMapper personnelProjectMapper;

    @Override
    public List<Project> list() {
        List<Project> projects = super.list();
        for (Project project : projects) {
            Long count = personnelProjectMapper.selectCount(
                    new LambdaQueryWrapper<PersonnelProject>().eq(PersonnelProject::getProjectId, project.getId()));
            project.setPersonnelCount(count.intValue());
        }
        return projects;
    }

    @Override
    public Map<String, Object> getProjectWithBudget(Long projectId) {
        Map<String, Object> result = new HashMap<>();
        Project project = getById(projectId);
        if (project == null) {
            return null;
        }
        result.put("project", project);
        result.put("totalBudget", calculateProjectTotalBudget(projectId));
        return result;
    }

    @Override
    public BigDecimal calculateProjectTotalBudget(Long projectId) {
        List<FeatureBreakdown> features = featureBreakdownMapper.selectList(
                new LambdaQueryWrapper<FeatureBreakdown>().eq(FeatureBreakdown::getProjectId, projectId));
        BigDecimal total = BigDecimal.ZERO;
        for (FeatureBreakdown feature : features) {
            total = total.add(calculateFeatureSubtotal(feature.getId()));
        }
        return total;
    }

    private BigDecimal calculateFeatureSubtotal(Long featureId) {
        List<BudgetItem> items = budgetItemMapper.selectList(
                new LambdaQueryWrapper<BudgetItem>().eq(BudgetItem::getFeatureId, featureId));
        BigDecimal subtotal = BigDecimal.ZERO;
        for (BudgetItem item : items) {
            subtotal = subtotal.add(item.getUnitCost().multiply(item.getQuantity()));
        }
        return subtotal;
    }
}
