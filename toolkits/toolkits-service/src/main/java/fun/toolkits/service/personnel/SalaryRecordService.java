package fun.toolkits.service.personnel;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.toolkits.model.entity.SalaryRecord;

import java.util.List;

public interface SalaryRecordService extends IService<SalaryRecord> {

    /**
     * 获取人员的薪资历史
     */
    List<SalaryRecord> getHistoryByPersonnelId(Long personnelId);

    /**
     * 新增薪资记录（校验金额>0）
     */
    SalaryRecord addSalaryRecord(SalaryRecord record);
}
