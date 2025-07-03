package su.taskmanager.data.workspace.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import su.taskmanager.data.workspace.entity.Objective;
import su.taskmanager.data.workspace.entity.Task;
import su.taskmanager.data.workspace.repository.ObjectiveRepository;
import su.taskmanager.data.workspace.repository.TaskRepository;

import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ObjectiveRepository objectiveRepository;

    public TaskService(TaskRepository taskRepository, ObjectiveRepository objectiveRepository) {
        this.taskRepository = taskRepository;
        this.objectiveRepository = objectiveRepository;
    }

    public Task createTask(Task task) {
        Long objectiveId = task.getObjective().getId();
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new EntityNotFoundException("Objective with this id not found " + objectiveId));
        task.setObjective(objective);
        return taskRepository.save(task);
    }
}
