package fun.toolkits.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("development_role")
public class DevelopmentRole {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String description;

    private Long createdAt;

    private Long updatedAt;
}
