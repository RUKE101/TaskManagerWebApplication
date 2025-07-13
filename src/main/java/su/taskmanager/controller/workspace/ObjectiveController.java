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
import su.taskmanager.data.workspace.service.ObjectiveService;
import su.taskmanager.data.workspace.service.WorkspaceService;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/workspace/project/objective")
public class ObjectiveController {
    private final ObjectiveService objectiveService;
    private final UserService userService;
    private final WorkspaceService workspaceService;


    @ResponseBody
    @PostMapping
    ResponseEntity<?> createObjective(@RequestBody ObjectiveCreateDto dto, @AuthenticationPrincipal User user) {
        Objective objective = new Objective();
        objectiveService.createObjective(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created objective");
    }

    @ResponseBody
    @PatchMapping("/{objectiveId}")
    public ResponseEntity<?> updateObjective(
            @RequestBody ObjectiveDto dto,
            @AuthenticationPrincipal User user,
            @PathVariable Long objectiveId) {

        Objective objective = objectiveService.getObjectiveById(objectiveId);
        if (!objectiveService.isUserAuthorized(user, objective)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not author or task not hanging on you");
        }

        if (!workspaceService.isUserInWorkspace(objective.getWorkspace(), user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You're not member of workspace");
        }
        User hangsOn = userService.getUserByUsername(dto.getHangsOn());
        if (!workspaceService.isUserInWorkspace(objective.getWorkspace(), hangsOn)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User to assign must be a member of the workspace");
        }
        objectiveService.updateObjective(dto, objective);
        return ResponseEntity.ok(dto);
    }

    @ResponseBody
    @DeleteMapping("{objectiveId}")
    ResponseEntity<?> deleteObjective(@PathVariable Long objectiveId, @AuthenticationPrincipal User user) {
        Objective objective = objectiveService.getObjectiveById(objectiveId);
        if (!workspaceService.isAuthor(objective.getWorkspace(), user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only author can delete objectives");
        }
        objectiveService.delete(objective);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Successfully deleted");
    }

}



