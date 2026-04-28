package fun.toolkits.service.project.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.toolkits.dal.mapper.FeatureBreakdownMapper;
import fun.toolkits.model.entity.FeatureBreakdown;
import fun.toolkits.service.project.FeatureBreakdownService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FeatureBreakdownServiceImpl extends ServiceImpl<FeatureBreakdownMapper, FeatureBreakdown>
        implements FeatureBreakdownService {

    private static final int MAX_DEPTH = 5;

    @Override
    public List<FeatureBreakdown> getFeatureTree(Long projectId) {
        List<FeatureBreakdown> all = list(new LambdaQueryWrapper<FeatureBreakdown>()
                .eq(FeatureBreakdown::getProjectId, projectId)
                .orderByAsc(FeatureBreakdown::getSortOrder));
        return buildTree(all, null);
    }

    @Override
    public FeatureBreakdown createFeature(FeatureBreakdown feature) {
        int depth = calculateDepth(feature.getParentId());
        if (depth >= MAX_DEPTH) {
            throw new RuntimeException("功能拆解最大深度不能超过" + MAX_DEPTH + "层");
        }
        long now = System.currentTimeMillis();
        feature.setCreatedAt(now);
        feature.setUpdatedAt(now);
        save(feature);
        return feature;
    }

    @Override
    public void reorderFeature(Long featureId, Long newParentId, Integer newSortOrder) {
        FeatureBreakdown feature = getById(featureId);
        if (feature == null) {
            throw new RuntimeException("功能节点不存在");
        }
        if (newParentId != null) {
            int depth = calculateDepth(newParentId);
            // +1 because this node adds one more level
            int currentDepth = calculateDepth(featureId);
            if (depth + 1 >= MAX_DEPTH) {
                throw new RuntimeException("移动后深度不能超过" + MAX_DEPTH + "层");
            }
        }
        feature.setParentId(newParentId);
        feature.setSortOrder(newSortOrder);
        feature.setUpdatedAt(System.currentTimeMillis());
        updateById(feature);
    }

    private List<FeatureBreakdown> buildTree(List<FeatureBreakdown> all, Long parentId) {
        List<FeatureBreakdown> result = new ArrayList<>();
        for (FeatureBreakdown f : all) {
            if ((parentId == null && f.getParentId() == null)
                    || (parentId != null && parentId.equals(f.getParentId()))) {
                result.add(f);
            }
        }
        return result;
    }

    private int calculateDepth(Long featureId) {
        int depth = 0;
        Long currentId = featureId;
        while (currentId != null && depth < MAX_DEPTH) {
            FeatureBreakdown feature = getById(currentId);
            if (feature == null) {
                break;
            }
            depth++;
            currentId = feature.getParentId();
        }
        return depth;
    }
}
