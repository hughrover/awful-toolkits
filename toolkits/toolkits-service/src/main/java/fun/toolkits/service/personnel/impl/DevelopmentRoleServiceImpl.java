package fun.toolkits.service.personnel.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.toolkits.dal.mapper.DevelopmentRoleMapper;
import fun.toolkits.dal.mapper.PersonnelMapper;
import fun.toolkits.model.entity.DevelopmentRole;
import fun.toolkits.model.entity.Personnel;
import fun.toolkits.service.personnel.DevelopmentRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DevelopmentRoleServiceImpl extends ServiceImpl<DevelopmentRoleMapper, DevelopmentRole>
        implements DevelopmentRoleService {

    private final PersonnelMapper personnelMapper;

    @Override
    public boolean hasAssignedPersonnel(Long roleId) {
        Long count = personnelMapper.selectCount(
                new LambdaQueryWrapper<Personnel>().eq(Personnel::getRoleId, roleId));
        return count != null && count > 0;
    }
}
