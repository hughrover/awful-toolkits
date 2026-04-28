package fun.toolkits.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("salary_record")
public class SalaryRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long personnelId;

    private BigDecimal amount;

    private LocalDate effectiveDate;

    private Long createdAt;
}
