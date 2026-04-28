package fun.toolkits.web.controller;

import fun.toolkits.model.common.ApiResult;
import fun.toolkits.model.entity.Personnel;
import fun.toolkits.service.personnel.PersonnelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/personnel")
@RequiredArgsConstructor
public class PersonnelController {

    private final PersonnelService personnelService;

    @GetMapping
    public ApiResult<List<Personnel>> list() {
        return ApiResult.ok(personnelService.list());
    }

    @GetMapping("/{id}")
    public ApiResult<Personnel> getById(@PathVariable Long id) {
        Personnel personnel = personnelService.getById(id);
        return personnel != null ? ApiResult.ok(personnel) : ApiResult.error(404, "人员不存在");
    }

    @PostMapping
    public ApiResult<Personnel> create(@RequestBody Personnel personnel) {
        long now = System.currentTimeMillis();
        personnel.setCreatedAt(now);
        personnel.setUpdatedAt(now);
        if (personnel.getStatus() == null) {
            personnel.setStatus(1);
        }
        personnelService.save(personnel);
        return ApiResult.ok(personnel);
    }

    @PutMapping("/{id}")
    public ApiResult<Personnel> update(@PathVariable Long id, @RequestBody Personnel personnel) {
        Personnel existing = personnelService.getById(id);
        if (existing == null) {
            return ApiResult.error(404, "人员不存在");
        }
        personnel.setId(id);
        personnel.setUpdatedAt(System.currentTimeMillis());
        personnelService.updateById(personnel);
        return ApiResult.ok(personnelService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        Personnel existing = personnelService.getById(id);
        if (existing == null) {
            return ApiResult.error(404, "人员不存在");
        }
        personnelService.removeById(id);
        return ApiResult.ok();
    }
}
