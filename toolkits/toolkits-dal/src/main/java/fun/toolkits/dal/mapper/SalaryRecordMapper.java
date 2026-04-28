package fun.toolkits.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.toolkits.model.entity.SalaryRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SalaryRecordMapper extends BaseMapper<SalaryRecord> {
}
