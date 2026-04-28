package fun.toolkits.service.project.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.toolkits.dal.mapper.BudgetItemMapper;
import fun.toolkits.model.entity.BudgetItem;
import fun.toolkits.service.project.BudgetItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetItemServiceImpl extends ServiceImpl<BudgetItemMapper, BudgetItem>
        implements BudgetItemService {

    @Override
    public List<BudgetItem> getByFeatureId(Long featureId) {
        return list(new LambdaQueryWrapper<BudgetItem>()
                .eq(BudgetItem::getFeatureId, featureId));
    }

    @Override
    public BigDecimal calculateFeatureSubtotal(Long featureId) {
        List<BudgetItem> items = getByFeatureId(featureId);
        BigDecimal subtotal = BigDecimal.ZERO;
        for (BudgetItem item : items) {
            subtotal = subtotal.add(item.getUnitCost().multiply(item.getQuantity()));
        }
        return subtotal;
    }
}
