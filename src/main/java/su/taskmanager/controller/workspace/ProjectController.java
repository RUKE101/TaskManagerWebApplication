package su.taskmanager.controller.workspace;

import lombok.Builder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su.taskmanager.data.workspace.dto.create.ProjectCreateDto;
import su.taskmanager.data.workspace.entity.Project;
import su.taskmanager.data.workspace.entity.Workspace;
import su.taskmanager.data.workspace.service.ProjectService;
import su.taskmanager.data.workspace.service.WorkspaceService;

@Builder
@RestController
@RequestMapping("api/v1/workspace/project")
public class ProjectController {
    private final ProjectService projectService;
    private final WorkspaceService workspaceService;



    @PostMapping
    public Project createProject(@RequestBody ProjectCreateDto dto) {
        Workspace workspace = workspaceService.findWorkspaceById(dto.getWorkspaceId())
                .orElseThrow(() -> new RuntimeException("Workspace not found with id " + dto.getWorkspaceId()));

        Project project = new Project();
        project.setWorkspace(workspace);
        project.setNameOfProject(dto.getNameOfProject());
        project.setDescription(dto.getDescription());
        return projectService.createProject(project);
    }


}
