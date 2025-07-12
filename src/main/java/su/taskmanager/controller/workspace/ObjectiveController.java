package su.taskmanager.controller.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.user.service.UserService;
import su.taskmanager.data.workspace.dto.create.ObjectiveCreateDto;
import su.taskmanager.data.workspace.dto.read.ObjectiveDto;
import su.taskmanager.data.workspace.entity.Objective;
import su.taskmanager.data.workspace.entity.Workspace;
import su.taskmanager.data.workspace.repository.ObjectiveRepository;
import su.taskmanager.data.workspace.service.ObjectiveService;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/workspace/project/objective")
public class ObjectiveController {
    private final ObjectiveService objectiveService;
    private final ObjectiveRepository objectiveRepository;
    private final UserService userService;


    @ResponseBody
    @PostMapping
    ResponseEntity<?> createObjective(@RequestBody ObjectiveCreateDto dto, @AuthenticationPrincipal User user) {
        Objective objective = new Objective();

        objectiveService.createObjective(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created objective");
    }

    @ResponseBody
    @PatchMapping("/{objectiveId}")
    ResponseEntity<ObjectiveDto> updateObjective(@RequestBody ObjectiveDto dto, @AuthenticationPrincipal User user, @PathVariable Long objectiveId) {
        Optional<Objective> objectiveOptional = objectiveService.findObjectiveById(objectiveId);
        if (objectiveOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Objective objective = objectiveOptional.get();
        if (!user.getUsername().equals(objective.getWorkspace().getAuthor().getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (dto.getHangsOn() != null && !dto.getHangsOn().isBlank()) {
            Workspace workspace = objective.getWorkspace();
            Optional<User> hangsOnOptional = userService.findByUsername(dto.getHangsOn());
            if (hangsOnOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            User hangsOn = hangsOnOptional.get();
            boolean userInWorkspace = workspace.getUsers().stream()
                    .anyMatch(users -> users.getUsername().equals(dto.getHangsOn()));
            if (!userInWorkspace) {
                return ResponseEntity.badRequest().build();
            }
            objective.setTaskHangsOn(hangsOn);
        }
        objectiveService.updateObjective(dto, objective);
        return ResponseEntity.ok().body(dto);
    }
    @ResponseBody
    @DeleteMapping("{objectiveId}")
    ResponseEntity<?> deleteObjective(@PathVariable Long objectiveId, @AuthenticationPrincipal User user) {
        Optional<Objective> objectiveOptional = objectiveRepository.findById(objectiveId);
        if (objectiveOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Not found objective with such id");
        }
        Objective objective = objectiveOptional.get();
        if (!objective.getWorkspace().getAuthor().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only workspace author can delete objectives in workspace");
        }
        objectiveRepository.delete(objective);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Successfully deleted");
    }
}
