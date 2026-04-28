package fun.toolkits.web.controller;

import fun.toolkits.model.common.ApiResult;
import fun.toolkits.model.entity.FeatureBreakdown;
import fun.toolkits.service.project.FeatureBreakdownService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/features")
@RequiredArgsConstructor
public class FeatureBreakdownController {

    private final FeatureBreakdownService featureBreakdownService;

    @GetMapping
    public ApiResult<List<FeatureBreakdown>> tree(@PathVariable Long projectId) {
        return ApiResult.ok(featureBreakdownService.getFeatureTree(projectId));
    }

    @PostMapping
    public ApiResult<FeatureBreakdown> create(@PathVariable Long projectId,
                                               @RequestBody FeatureBreakdown feature) {
        feature.setProjectId(projectId);
        try {
            FeatureBreakdown created = featureBreakdownService.createFeature(feature);
            return ApiResult.ok(created);
        } catch (RuntimeException e) {
            return ApiResult.error(400, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResult<FeatureBreakdown> update(@PathVariable Long projectId,
                                               @PathVariable Long id,
                                               @RequestBody FeatureBreakdown feature) {
        FeatureBreakdown existing = featureBreakdownService.getById(id);
        if (existing == null || !existing.getProjectId().equals(projectId)) {
            return ApiResult.error(404, "功能节点不存在");
        }
        feature.setId(id);
        feature.setProjectId(projectId);
        feature.setUpdatedAt(System.currentTimeMillis());
        featureBreakdownService.updateById(feature);
        return ApiResult.ok(featureBreakdownService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long projectId, @PathVariable Long id) {
        FeatureBreakdown existing = featureBreakdownService.getById(id);
        if (existing == null || !existing.getProjectId().equals(projectId)) {
            return ApiResult.error(404, "功能节点不存在");
        }
        featureBreakdownService.removeById(id);
        return ApiResult.ok();
    }

    @PutMapping("/{id}/reorder")
    public ApiResult<Void> reorder(@PathVariable Long projectId,
                                    @PathVariable Long id,
                                    @RequestParam(required = false) Long newParentId,
                                    @RequestParam Integer newSortOrder) {
        try {
            featureBreakdownService.reorderFeature(id, newParentId, newSortOrder);
            return ApiResult.ok();
        } catch (RuntimeException e) {
            return ApiResult.error(400, e.getMessage());
        }
    }
}
