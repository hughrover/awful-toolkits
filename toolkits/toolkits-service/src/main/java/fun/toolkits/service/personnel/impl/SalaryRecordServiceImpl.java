package fun.toolkits.service.personnel.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.toolkits.dal.mapper.SalaryRecordMapper;
import fun.toolkits.model.entity.SalaryRecord;
import fun.toolkits.service.personnel.SalaryRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaryRecordServiceImpl extends ServiceImpl<SalaryRecordMapper, SalaryRecord>
        implements SalaryRecordService {

    @Override
    public List<SalaryRecord> getHistoryByPersonnelId(Long personnelId) {
        return list(new LambdaQueryWrapper<SalaryRecord>()
                .eq(SalaryRecord::getPersonnelId, personnelId)
                .orderByDesc(SalaryRecord::getEffectiveDate));
    }

    @Override
    public SalaryRecord addSalaryRecord(SalaryRecord record) {
        if (record.getAmount() == null || record.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("薪资金额必须大于0");
        }
        record.setCreatedAt(System.currentTimeMillis());
        save(record);
        return record;
    }
}
