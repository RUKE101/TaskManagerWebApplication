package su.taskmanager.data.workspace.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.workspace.entity.Objective;
import su.taskmanager.data.workspace.entity.Project;
import su.taskmanager.data.workspace.repository.ObjectiveRepository;
import su.taskmanager.data.workspace.repository.ProjectRepository;

import java.util.Optional;

@Service
public class ObjectiveService {
    private final ProjectRepository projectRepository;
    private final ObjectiveRepository objectiveRepository;

    public  ObjectiveService(ProjectRepository projectRepository, ObjectiveRepository objectiveRepository) {
        this.projectRepository = projectRepository;
        this.objectiveRepository = objectiveRepository;
    }

    public Objective createObjective(Objective objective) {
        Long projectId = objective.getProject().getId();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Not found project with id: "+ projectId));
        objective.setProject(project);
        return objectiveRepository.save(objective);
    }

    public Optional<Objective> findObjectiveById(Long id) {
        return objectiveRepository.findById(id);
    }

}
