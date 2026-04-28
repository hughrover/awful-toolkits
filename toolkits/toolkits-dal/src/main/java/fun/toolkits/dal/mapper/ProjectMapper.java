package fun.toolkits.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.toolkits.model.entity.Project;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
}
