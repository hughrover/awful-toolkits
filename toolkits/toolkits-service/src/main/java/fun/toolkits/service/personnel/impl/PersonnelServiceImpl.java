package fun.toolkits.service.personnel.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.toolkits.dal.mapper.PersonnelMapper;
import fun.toolkits.model.entity.Personnel;
import fun.toolkits.service.personnel.PersonnelService;
import org.springframework.stereotype.Service;

@Service
public class PersonnelServiceImpl extends ServiceImpl<PersonnelMapper, Personnel>
        implements PersonnelService {
}
