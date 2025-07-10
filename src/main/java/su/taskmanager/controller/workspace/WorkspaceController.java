package su.taskmanager.controller.workspace;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import su.taskmanager.data.user.service.UserService;


import org.springframework.web.bind.annotation.*;

import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.workspace.entity.Workspace;
import su.taskmanager.data.workspace.dto.create.WorkspaceCreateDto;
import su.taskmanager.data.workspace.service.WorkspaceService;

@RestController
@RequestMapping("api/v1/workspace")
public class WorkspaceController {
    private final WorkspaceService workspaceService;
    private final UserService userService;


    public WorkspaceController(WorkspaceService workspaceService, UserService userService) {
        this.workspaceService = workspaceService;
        this.userService = userService;
    }
    @PostMapping
    public WorkspaceCreateDto createWorkspace(@RequestBody WorkspaceCreateDto dto, @AuthenticationPrincipal User user) {
        User author = user;
        Workspace workspace = new Workspace();
        workspace.setAuthor(author);
        System.out.println("Author id: " + workspace.getAuthor().getId());
        workspace.setNameOfWorkspace(dto.getNameOfWorkspace());
        workspace.setDescription(dto.getDescription());
        workspaceService.createWorkspace(workspace);
        return dto;
    }

}
