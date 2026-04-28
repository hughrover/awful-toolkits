package fun.toolkits.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.toolkits.model.entity.Personnel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PersonnelMapper extends BaseMapper<Personnel> {
}
