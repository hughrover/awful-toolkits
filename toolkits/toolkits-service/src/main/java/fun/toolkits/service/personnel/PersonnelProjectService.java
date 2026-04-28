package fun.toolkits.service.personnel;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.toolkits.model.entity.PersonnelProject;

import java.util.List;

public interface PersonnelProjectService extends IService<PersonnelProject> {

    /**
     * 获取项目团队成员
     */
    List<PersonnelProject> getProjectTeam(Long projectId);

    /**
     * 获取人员参与的项目
     */
    List<PersonnelProject> getPersonnelProjects(Long personnelId);
}
