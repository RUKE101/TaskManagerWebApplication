package su.taskmanager.controller.workspace;

import lombok.Builder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su.taskmanager.data.workspace.dto.create.ObjectiveCreateDto;
import su.taskmanager.data.workspace.entity.Objective;
import su.taskmanager.data.workspace.entity.Project;
import su.taskmanager.data.workspace.service.ObjectiveService;
import su.taskmanager.data.workspace.service.ProjectService;

@Builder
@RestController
@RequestMapping("api/v1/workspace/project/objective")
public class ObjectiveController {
    private final ObjectiveService objectiveService;
    private final ProjectService projectService;

    @PostMapping
    Objective createObjective(@RequestBody ObjectiveCreateDto dto) {
        Project project = projectService.findProjectById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Not found project with such id " + dto.getProjectId()));
        Objective objective = new Objective();
        objective.setProject(project);
        objective.setNameOfObjective(dto.getNameOfProject());
        objective.setDescription(dto.getDescription());
        return objectiveService.createObjective(objective);
    }
}
