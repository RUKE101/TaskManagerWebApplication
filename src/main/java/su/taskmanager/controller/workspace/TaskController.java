package su.taskmanager.controller.workspace;

import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su.taskmanager.data.workspace.dto.create.TaskCreateDto;
import su.taskmanager.data.workspace.entity.Objective;
import su.taskmanager.data.workspace.entity.Task;
import su.taskmanager.data.workspace.service.ObjectiveService;
import su.taskmanager.data.workspace.service.TaskService;

@Builder
@RestController
@RequestMapping("api/v1/workspace/project/objective/task")
public class TaskController {
    private final TaskService taskService;
    private final ObjectiveService objectiveService;

    @PostMapping
    public Task createTask(@RequestBody TaskCreateDto taskCreateDto) {
        Objective objective = objectiveService.findObjectiveById(taskCreateDto.getObjectiveId())
                .orElseThrow(() -> new EntityNotFoundException("Not found objective with id: "+ taskCreateDto.getObjectiveId()));
        Task task = new Task();
        task.setObjective(objective);
        task.setDescription(taskCreateDto.getDescription());
        task.setNameOfTask(taskCreateDto.getNameOfTask());
        return taskService.createTask(task);
    }
}
