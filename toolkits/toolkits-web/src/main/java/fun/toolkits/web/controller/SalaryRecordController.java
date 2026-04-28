package fun.toolkits.web.controller;

import fun.toolkits.model.common.ApiResult;
import fun.toolkits.model.entity.SalaryRecord;
import fun.toolkits.service.personnel.SalaryRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/personnel/{personnelId}/salaries")
@RequiredArgsConstructor
public class SalaryRecordController {

    private final SalaryRecordService salaryRecordService;

    @GetMapping
    public ApiResult<List<SalaryRecord>> history(@PathVariable Long personnelId) {
        return ApiResult.ok(salaryRecordService.getHistoryByPersonnelId(personnelId));
    }

    @PostMapping
    public ApiResult<SalaryRecord> create(@PathVariable Long personnelId,
                                           @RequestBody SalaryRecord record) {
        record.setPersonnelId(personnelId);
        try {
            SalaryRecord saved = salaryRecordService.addSalaryRecord(record);
            return ApiResult.ok(saved);
        } catch (RuntimeException e) {
            return ApiResult.error(400, e.getMessage());
        }
    }
}
