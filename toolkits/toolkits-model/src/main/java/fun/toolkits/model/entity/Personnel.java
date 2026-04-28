package fun.toolkits.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("personnel")
public class Personnel {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String email;

    private String phone;

    private LocalDate hireDate;

    private Long roleId;

    /** 状态: 0-离职, 1-在职 */
    private Integer status;

    private Long createdAt;

    private Long updatedAt;
}
