package fun.toolkits.web.controller;

import fun.toolkits.model.common.ApiResult;
import fun.toolkits.model.entity.BudgetItem;
import fun.toolkits.service.project.BudgetItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/features/{featureId}/budget-items")
@RequiredArgsConstructor
public class BudgetItemController {

    private final BudgetItemService budgetItemService;

    @GetMapping
    public ApiResult<List<BudgetItem>> list(@PathVariable Long featureId) {
        return ApiResult.ok(budgetItemService.getByFeatureId(featureId));
    }

    @PostMapping
    public ApiResult<BudgetItem> create(@PathVariable Long featureId,
                                         @RequestBody BudgetItem item) {
        item.setFeatureId(featureId);
        long now = System.currentTimeMillis();
        item.setCreatedAt(now);
        item.setUpdatedAt(now);
        budgetItemService.save(item);
        return ApiResult.ok(item);
    }

    @PutMapping("/{id}")
    public ApiResult<BudgetItem> update(@PathVariable Long featureId,
                                         @PathVariable Long id,
                                         @RequestBody BudgetItem item) {
        BudgetItem existing = budgetItemService.getById(id);
        if (existing == null || !existing.getFeatureId().equals(featureId)) {
            return ApiResult.error(404, "预算项不存在");
        }
        item.setId(id);
        item.setFeatureId(featureId);
        item.setUpdatedAt(System.currentTimeMillis());
        budgetItemService.updateById(item);
        return ApiResult.ok(budgetItemService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long featureId, @PathVariable Long id) {
        BudgetItem existing = budgetItemService.getById(id);
        if (existing == null || !existing.getFeatureId().equals(featureId)) {
            return ApiResult.error(404, "预算项不存在");
        }
        budgetItemService.removeById(id);
        return ApiResult.ok();
    }

    @GetMapping("/subtotal")
    public ApiResult<BigDecimal> subtotal(@PathVariable Long featureId) {
        return ApiResult.ok(budgetItemService.calculateFeatureSubtotal(featureId));
    }
}
