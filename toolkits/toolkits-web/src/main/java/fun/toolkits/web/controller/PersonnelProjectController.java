package fun.toolkits.web.controller;

import fun.toolkits.model.common.ApiResult;
import fun.toolkits.model.entity.PersonnelProject;
import fun.toolkits.service.personnel.PersonnelProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PersonnelProjectController {

    private final PersonnelProjectService personnelProjectService;

    @GetMapping("/projects/{projectId}/team")
    public ApiResult<List<PersonnelProject>> projectTeam(@PathVariable Long projectId) {
        return ApiResult.ok(personnelProjectService.getProjectTeam(projectId));
    }

    @PostMapping("/projects/{projectId}/team")
    public ApiResult<PersonnelProject> assignToProject(@PathVariable Long projectId,
                                                        @RequestBody PersonnelProject assignment) {
        assignment.setProjectId(projectId);
        assignment.setCreatedAt(System.currentTimeMillis());
        personnelProjectService.save(assignment);
        return ApiResult.ok(assignment);
    }

    @DeleteMapping("/personnel-project/{id}")
    public ApiResult<Void> removeFromProject(@PathVariable Long id) {
        personnelProjectService.removeById(id);
        return ApiResult.ok();
    }

    @GetMapping("/personnel/{personnelId}/projects")
    public ApiResult<List<PersonnelProject>> personnelProjects(@PathVariable Long personnelId) {
        return ApiResult.ok(personnelProjectService.getPersonnelProjects(personnelId));
    }
}
