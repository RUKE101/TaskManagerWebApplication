package su.taskmanager.controller.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import su.taskmanager.data.user.dto.read.UserDto;
import su.taskmanager.data.user.service.UserService;


import org.springframework.web.bind.annotation.*;

import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.workspace.dto.read.WorkspaceDto;
import su.taskmanager.data.workspace.entity.Workspace;
import su.taskmanager.data.workspace.dto.create.WorkspaceCreateDto;
import su.taskmanager.data.workspace.service.WorkspaceService;

import org.springframework.security.access.AccessDeniedException;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/workspace")
public class WorkspaceController {
    private final WorkspaceService workspaceService;
    private final UserService userService;

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<WorkspaceDto> createWorkspace(@RequestBody WorkspaceCreateDto dto,
                                                        @AuthenticationPrincipal User user) {
        Workspace workspace = new Workspace();
        workspace.setAuthor(user);
        workspace.setNameOfWorkspace(dto.getNameOfWorkspace());
        workspace.setDescription(dto.getDescription());
        WorkspaceDto response = workspaceService.createWorkspace(workspace);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ResponseBody
    @GetMapping("/{workspaceId}")
    public ResponseEntity<?> getWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal User user) {
        Workspace workspace = workspaceService.getWorkspace(workspaceId);

        boolean userInWorkspace = workspaceService.isUserInWorkspace(workspace, user);
        if (!userInWorkspace) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You're not in workspace");
        }
        WorkspaceDto dto = workspaceService.getWorkspaceDtoById(workspaceId);
        return ResponseEntity.ok(dto);
    }

    @ResponseBody
    @PatchMapping("/{workspaceId}")
    public ResponseEntity<?> updateWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal User user,
                                             @RequestBody WorkspaceDto dto) {
        Workspace workspace = workspaceService.getWorkspace(workspaceId);
        isAuthorOfWorkspace(user, workspace);
        WorkspaceDto response = workspaceService.updateWorkspace(workspace, dto);
        return ResponseEntity.ok(response);
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
    public ResponseEntity<?> changeAuthorOfWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal User user,
                                                     @RequestBody WorkspaceDto dto)
    {
        Workspace workspace = workspaceService.getWorkspace(workspaceId);
        isAuthorOfWorkspace(user, workspace);
        User newAuthor = userService.getUserById(dto.getAuthorId());

        if (!workspaceService.isUserInWorkspace(workspace, newAuthor)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("New author is not in workspace");
        }

        WorkspaceDto response = workspaceService.updateWorkspaceAuthor(workspace, newAuthor);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{workspaceId}/user")
    public ResponseEntity<?> deleteUserFromWorkspace(@PathVariable Long workspaceId,
                                                     @AuthenticationPrincipal User user,
                                                     @RequestBody UserDto dtoToRemove) {
        Workspace workspace = workspaceService.getWorkspace(workspaceId);
        isAuthorOfWorkspace(user, workspace);
        WorkspaceDto response = workspaceService.removeUserFromWorkspace(workspace, user, dtoToRemove);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public boolean isAuthorOfWorkspace(User user, Workspace workspace) {
        if (!workspaceService.isAuthor(workspace, user)) {
            throw new AccessDeniedException("Only author can change workspace properties");
        }
        return true;
    }

}
