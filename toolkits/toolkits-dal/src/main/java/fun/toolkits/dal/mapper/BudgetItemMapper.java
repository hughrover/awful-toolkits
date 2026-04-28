package fun.toolkits.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.toolkits.model.entity.BudgetItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BudgetItemMapper extends BaseMapper<BudgetItem> {
}
