package fun.toolkits.web.controller;

import fun.toolkits.model.common.ApiResult;
import fun.toolkits.model.entity.DevelopmentRole;
import fun.toolkits.service.personnel.DevelopmentRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class DevelopmentRoleController {

    private final DevelopmentRoleService developmentRoleService;

    @GetMapping
    public ApiResult<List<DevelopmentRole>> list() {
        return ApiResult.ok(developmentRoleService.list());
    }

    @PostMapping
    public ApiResult<DevelopmentRole> create(@RequestBody DevelopmentRole role) {
        long now = System.currentTimeMillis();
        role.setCreatedAt(now);
        role.setUpdatedAt(now);
        developmentRoleService.save(role);
        return ApiResult.ok(role);
    }

    @PutMapping("/{id}")
    public ApiResult<DevelopmentRole> update(@PathVariable Long id,
                                              @RequestBody DevelopmentRole role) {
        DevelopmentRole existing = developmentRoleService.getById(id);
        if (existing == null) {
            return ApiResult.error(404, "角色不存在");
        }
        role.setId(id);
        role.setUpdatedAt(System.currentTimeMillis());
        developmentRoleService.updateById(role);
        return ApiResult.ok(developmentRoleService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        DevelopmentRole existing = developmentRoleService.getById(id);
        if (existing == null) {
            return ApiResult.error(404, "角色不存在");
        }
        if (developmentRoleService.hasAssignedPersonnel(id)) {
            return ApiResult.error(400, "该角色下有人员，无法删除");
        }
        developmentRoleService.removeById(id);
        return ApiResult.ok();
    }
}
