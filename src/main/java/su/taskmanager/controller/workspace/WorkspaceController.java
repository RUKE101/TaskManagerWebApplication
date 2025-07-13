package su.taskmanager.controller.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import su.taskmanager.data.user.service.UserService;


import org.springframework.web.bind.annotation.*;

import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.workspace.dto.read.WorkspaceDto;
import su.taskmanager.data.workspace.entity.Workspace;
import su.taskmanager.data.workspace.dto.create.WorkspaceCreateDto;
import su.taskmanager.data.workspace.service.WorkspaceService;
import su.taskmanager.mappers.WorkspaceMapper;

import org.springframework.security.access.AccessDeniedException;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/workspace")
public class WorkspaceController {
    private final WorkspaceService workspaceService;
    private final UserService userService;

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<WorkspaceCreateDto> createWorkspace(@RequestBody WorkspaceCreateDto dto, @AuthenticationPrincipal User user) {
        Workspace workspace = new Workspace();
        workspace.setAuthor(user);
        workspace.setNameOfWorkspace(dto.getNameOfWorkspace());
        workspace.setDescription(dto.getDescription());
        workspaceService.createWorkspace(workspace);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @ResponseBody
    @GetMapping("/{workspaceId}")
    public ResponseEntity<?> getWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal User user) {
        Workspace workspace = workspaceService.getWorkspace(workspaceId);

        boolean userInWorkspace = workspaceService.isUserInWorkspace(workspace, user);
        if (!userInWorkspace) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You're not in workspace");
        }
        WorkspaceDto dto = WorkspaceMapper.toDto(workspace);
        return ResponseEntity.ok(dto);
    }

    @ResponseBody
    @PatchMapping("/{workspaceId}")
    public ResponseEntity<?> updateWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal User user,
                                             @RequestBody WorkspaceDto dto) {
        Workspace workspace = workspaceService.getWorkspace(workspaceId);
        isAuthorOfWorkspace(user, workspace);
        workspaceService.updateWorkspace(workspace, dto);
        return ResponseEntity.ok("Updated workspace with id: " + workspaceId);
    }

    @ResponseBody
    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<?> deleteWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal User user) {
        Workspace workspace = workspaceService.getWorkspace(workspaceId);
        isAuthorOfWorkspace(user, workspace);
        workspaceService.deleteWorkspace(workspace);
        return ResponseEntity.ok("Workspace deleted successfully");

    }

    @ResponseBody
    @PutMapping("/{workspaceId}")
    public ResponseEntity<?> changeAuthorOfWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal User user, @RequestBody WorkspaceDto dto)
    {
        Workspace workspace = workspaceService.getWorkspace(workspaceId);
        isAuthorOfWorkspace(user, workspace);
        User newAuthor = userService.getUserById(dto.getAuthorId());

        if (!workspaceService.isUserInWorkspace(workspace, newAuthor)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("New author is not in workspace");
        }

        workspaceService.updateWorkspaceAuthor(workspace, newAuthor);
        return ResponseEntity.status(HttpStatus.OK).body("Changed workspace author successfully");
    }

    public boolean isAuthorOfWorkspace(User user, Workspace workspace) {
        if (!workspaceService.isAuthor(workspace, user)) {
            throw new AccessDeniedException("Only author can change workspace properties");
        }
        return true;
    }

}
