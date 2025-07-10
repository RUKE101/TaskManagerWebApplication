package su.taskmanager.controller.workspace;

import lombok.Builder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su.taskmanager.data.workspace.dto.create.ObjectiveCreateDto;
import su.taskmanager.data.workspace.dto.read.ObjectiveDto;
import su.taskmanager.data.workspace.entity.Objective;
import su.taskmanager.data.workspace.service.ObjectiveService;

@Builder
@RestController
@RequestMapping("api/v1/workspace/project/objective")
public class ObjectiveController {
    private final ObjectiveService objectiveService;

    @PostMapping
    ObjectiveDto createObjective(@RequestBody ObjectiveCreateDto dto) {
        Objective objective = new Objective();



        return objectiveService.createObjective(dto);
    }
}
