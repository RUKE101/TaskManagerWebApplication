package su.taskmanager.controller.workspace;

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
    public Workspace createWorkspace(@RequestBody WorkspaceCreateDto dto) {
        User author = userService.findUserById(dto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("User not found with id " + dto.getAuthorId()));
        Workspace workspace = new Workspace();


        workspace.setAuthor(author);
        System.out.println("Author id: " + workspace.getAuthor().getId());
        workspace.setNameOfWorkspace(dto.getNameOfWorkspace());
        workspace.setDescription(dto.getDescription());
        return workspaceService.createWorkspace(workspace);
    }

}
