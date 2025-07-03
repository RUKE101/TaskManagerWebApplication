package su.taskmanager.data.workspace.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import su.taskmanager.data.workspace.entity.Project;
import su.taskmanager.data.workspace.entity.Workspace;
import su.taskmanager.data.workspace.repository.ProjectRepository;
import su.taskmanager.data.workspace.repository.WorkspaceRepository;

import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final WorkspaceRepository workspaceRepository;

    public ProjectService(ProjectRepository projectRepository, WorkspaceRepository workspaceRepository) {
        this.projectRepository = projectRepository;
        this.workspaceRepository = workspaceRepository;
    }

    public Project createProject(Project project) {
        Long workspaceId = project.getWorkspace().getId();
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new EntityNotFoundException("Not found workspace with id: "+ workspaceId));
        project.setWorkspace(workspace);
        return projectRepository.save(project);
    }

    public Optional<Project> findProjectById(Long id) {
        return projectRepository.findById(id);
    }
}
