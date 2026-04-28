package fun.toolkits.service.personnel;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.toolkits.model.entity.DevelopmentRole;

public interface DevelopmentRoleService extends IService<DevelopmentRole> {

    /**
     * 检查角色是否有关联人员
     */
    boolean hasAssignedPersonnel(Long roleId);
}
