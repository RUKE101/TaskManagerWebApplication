package su.taskmanager.controller.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import su.taskmanager.data.user.service.UserService;


import org.springframework.web.bind.annotation.*;

import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.workspace.dto.read.WorkspaceDto;
import su.taskmanager.data.workspace.entity.Workspace;
import su.taskmanager.data.workspace.dto.create.WorkspaceCreateDto;
import su.taskmanager.data.workspace.repository.WorkspaceRepository;
import su.taskmanager.data.workspace.service.WorkspaceService;
import su.taskmanager.mappers.WorkspaceMapper;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/workspace")
public class WorkspaceController {
    private final WorkspaceService workspaceService;
    private final UserService userService;
    private final WorkspaceRepository workspaceRepository;

    @ResponseBody
    @GetMapping("/{workspaceId}")
    public ResponseEntity<?> getWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal User user) {
        Optional<Workspace> workspaceOptional = workspaceRepository.findById(workspaceId);
        if (workspaceOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found workspace with id: " + workspaceId);
        }
        Workspace workspace = workspaceOptional.get();

        boolean userInWorkspace = workspace.getUsers().stream()
                .anyMatch(users -> users.getId().equals(user.getId()));
        if (!userInWorkspace) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You're not in workspace");
        }
        WorkspaceDto dto = WorkspaceMapper.toDto(workspace);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public WorkspaceCreateDto createWorkspace(@RequestBody WorkspaceCreateDto dto, @AuthenticationPrincipal User user) {
        Workspace workspace = new Workspace();
        workspace.setAuthor(user);
        workspace.setNameOfWorkspace(dto.getNameOfWorkspace());
        workspace.setDescription(dto.getDescription());
        workspaceService.createWorkspace(workspace);
        return dto;
    }
    @ResponseBody
    @PatchMapping("/{workspaceId}")
    public ResponseEntity<?> updateWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal User user,
                                             @RequestBody WorkspaceDto dto) {
        Optional<Workspace> workspaceOptional = workspaceService.findById(workspaceId);
        if (workspaceOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found workspace with id: " + workspaceId);
        }

        Workspace workspace = workspaceOptional.get();
        if (!user.getUsername().equals(workspace.getAuthor().getUsername())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only author of workspace can update workspace fields with workspace id: " + workspaceId);
        }
        workspaceService.updateWorkspace(workspace, dto);
        return ResponseEntity.ok("Updated workspace with id: " + workspaceId);
    }
    @Transactional
    @ResponseBody
    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<?> deleteWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal User user) {
        Optional<Workspace> workspaceOptional = workspaceService.findById(workspaceId);
        if (workspaceOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found workspace with id: " + workspaceId);
        }
        Workspace workspace = workspaceOptional.get();
        if (!user.getUsername().equals(workspace.getAuthor().getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only author of workspace can delete workspace with id: " + workspaceId);
        }
        for (User users : workspace.getUsers()) {
            users.getWorkspaces().remove(workspace);
        }

        workspace.getInvites().clear();
        workspace.getUsers().clear();
        workspaceRepository.delete(workspace);
        return ResponseEntity.ok("Workspace deleted successfully");

    }

    @ResponseBody
    @PutMapping("/{workspaceId}")
    public ResponseEntity<?> changeAuthorOfWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal User user, @RequestBody WorkspaceDto dto)
    {
        Optional<Workspace> workspaceOptional = workspaceService.findById(workspaceId);
        if (workspaceOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found workspace with id: " + workspaceId);
        }
        Workspace workspace = workspaceOptional.get();
        if (!workspace.getAuthor().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only workspace author allowed to change author");
        }
        Optional<User> userOptional = userService.findUserById(dto.getAuthorId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found user with id: " + dto.getAuthorId());
        }
        User newAuthor = userOptional.get();
        boolean userInWorkspace = workspace.getUsers().stream()
                .anyMatch(users -> users.getId().equals(dto.getAuthorId()));
        if (!userInWorkspace) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("New author is not in workspace");
        }


        workspace.setAuthor(newAuthor);
        workspaceRepository.save(workspace);
        return ResponseEntity.status(HttpStatus.OK).body("Changed workspace author successfully");
    }
}
