package fun.toolkits.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("budget_item")
public class BudgetItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long featureId;

    private String name;

    private String unit;

    private BigDecimal unitCost;

    private BigDecimal quantity;

    private Long createdAt;

    private Long updatedAt;
}
