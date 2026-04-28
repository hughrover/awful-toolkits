package fun.toolkits.service.project;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.toolkits.model.entity.FeatureBreakdown;

import java.util.List;

public interface FeatureBreakdownService extends IService<FeatureBreakdown> {

    /**
     * 获取项目的完整WBS树
     */
    List<FeatureBreakdown> getFeatureTree(Long projectId);

    /**
     * 创建功能节点，校验深度不超过5层
     */
    FeatureBreakdown createFeature(FeatureBreakdown feature);

    /**
     * 更新功能节点排序
     */
    void reorderFeature(Long featureId, Long newParentId, Integer newSortOrder);
}
