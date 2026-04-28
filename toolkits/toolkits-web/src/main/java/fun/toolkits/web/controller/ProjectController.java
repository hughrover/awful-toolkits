package fun.toolkits.web.controller;

import fun.toolkits.model.common.ApiResult;
import fun.toolkits.model.entity.Project;
import fun.toolkits.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ApiResult<List<Project>> list() {
        return ApiResult.ok(projectService.list());
    }

    @GetMapping("/{id}")
    public ApiResult<Project> getById(@PathVariable Long id) {
        Project project = projectService.getById(id);
        return project != null ? ApiResult.ok(project) : ApiResult.error(404, "项目不存在");
    }

    @PostMapping
    public ApiResult<Project> create(@RequestBody Project project) {
        long now = System.currentTimeMillis();
        project.setCreatedAt(now);
        project.setUpdatedAt(now);
        if (project.getStatus() == null) {
            project.setStatus(0);
        }
        projectService.save(project);
        return ApiResult.ok(project);
    }

    @PutMapping("/{id}")
    public ApiResult<Project> update(@PathVariable Long id, @RequestBody Project project) {
        Project existing = projectService.getById(id);
        if (existing == null) {
            return ApiResult.error(404, "项目不存在");
        }
        project.setId(id);
        project.setUpdatedAt(System.currentTimeMillis());
        projectService.updateById(project);
        return ApiResult.ok(projectService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        Project existing = projectService.getById(id);
        if (existing == null) {
            return ApiResult.error(404, "项目不存在");
        }
        projectService.removeById(id);
        return ApiResult.ok();
    }

    @GetMapping("/{id}/budget-total")
    public ApiResult<BigDecimal> getBudgetTotal(@PathVariable Long id) {
        return ApiResult.ok(projectService.calculateProjectTotalBudget(id));
    }
}
