package fun.toolkits.service.project;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.toolkits.model.entity.BudgetItem;

import java.math.BigDecimal;
import java.util.List;

public interface BudgetItemService extends IService<BudgetItem> {

    /**
     * 获取功能下的所有预算项
     */
    List<BudgetItem> getByFeatureId(Long featureId);

    /**
     * 计算功能子项小计
     */
    BigDecimal calculateFeatureSubtotal(Long featureId);
}
