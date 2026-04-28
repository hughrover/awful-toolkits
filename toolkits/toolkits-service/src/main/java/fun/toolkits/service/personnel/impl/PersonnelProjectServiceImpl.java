package fun.toolkits.service.personnel.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.toolkits.dal.mapper.PersonnelProjectMapper;
import fun.toolkits.model.entity.PersonnelProject;
import fun.toolkits.service.personnel.PersonnelProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonnelProjectServiceImpl extends ServiceImpl<PersonnelProjectMapper, PersonnelProject>
        implements PersonnelProjectService {

    @Override
    public List<PersonnelProject> getProjectTeam(Long projectId) {
        return list(new LambdaQueryWrapper<PersonnelProject>()
                .eq(PersonnelProject::getProjectId, projectId));
    }

    @Override
    public List<PersonnelProject> getPersonnelProjects(Long personnelId) {
        return list(new LambdaQueryWrapper<PersonnelProject>()
                .eq(PersonnelProject::getPersonnelId, personnelId));
    }
}
