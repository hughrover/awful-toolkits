package fun.toolkits.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("personnel_project")
public class PersonnelProject {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long personnelId;

    private Long projectId;

    private String roleInProject;

    private Long createdAt;
}
