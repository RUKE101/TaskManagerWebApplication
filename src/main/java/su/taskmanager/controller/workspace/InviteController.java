package su.taskmanager.controller.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.user.service.UserService;
import su.taskmanager.data.workspace.InvitationStatus;
import su.taskmanager.data.workspace.dto.create.InviteCreateDto;
import su.taskmanager.data.workspace.entity.Invite;
import su.taskmanager.data.workspace.entity.Workspace;
import su.taskmanager.data.workspace.service.InviteService;
import su.taskmanager.data.workspace.service.WorkspaceService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/workspace/invite")
public class InviteController {
    private final InviteService inviteService;
    private final UserService userService;
    private final WorkspaceService workspaceService;


    @GetMapping("/{uuid}")
    public ResponseEntity<?> getInvite(@PathVariable UUID uuid) {
        Invite invite = inviteService.findInviteByUuid(uuid);
        isExpired(invite);
        return ResponseEntity.ok(invite);
    }
    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<?> inviteCreate(@RequestBody InviteCreateDto dto, @AuthenticationPrincipal User user) {

        Workspace workspace = workspaceService.getWorkspace(dto.getWorkspace_id());
        isAuthor(workspace, user);
        Invite invite  = inviteService.createInvite(workspace, user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created invite with UUID: "
                + invite.getUuid());
    }

    @ResponseBody
    @PostMapping("/{uuid}/accept")
    public ResponseEntity<?> acceptInvitation(@PathVariable UUID uuid, @AuthenticationPrincipal User user) {
    Invite invite = inviteService.findInviteByUuid(uuid);
    isExpired(invite);
    Workspace workspace = workspaceService.getWorkspace(invite.getWorkspace().getId());
    if (workspaceService.isUserInWorkspace(workspace, user)) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("User already exists in the workspace");
    }

    workspaceService.addUserToWorkspace(invite.getWorkspace(), user);
    invite.setStatus(InvitationStatus.ACCEPTED);
    inviteService.save(invite);
    user.addWorkspace(invite.getWorkspace());
    userService.save(user);
    return ResponseEntity.ok("Accepted successfully");
    }

    @ResponseBody
    @PostMapping("/{uuid}/decline")
    public ResponseEntity<?> declineInvitation(@PathVariable UUID uuid, @AuthenticationPrincipal User user) {

        Invite invite = inviteService.findInviteByUuid(uuid);
        isExpired(invite);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User must be authenticated");
        }
        invite.setStatus(InvitationStatus.DECLINED);
        inviteService.save(invite);
        return ResponseEntity.ok("Declined successfully");
    }

    @DeleteMapping("{uuid}/delete")
    public ResponseEntity<?> deleteInvitation(@PathVariable UUID uuid, @AuthenticationPrincipal User user) {
        Invite invite = inviteService.findInviteByUuid(uuid);
        Workspace workspace = workspaceService.getWorkspace(invite.getWorkspace().getId());
        isAuthor(workspace, user);
        inviteService.deleteInvite(uuid);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted invitation");

    }

    public Boolean isExpired(Invite invite) {
        if (invite.getStatus() != InvitationStatus.PENDING) {
            throw new RuntimeException("Invite expired");
        }
        else return false;
    }

    public Boolean isAuthor(Workspace workspace, User user) {
        if (!workspaceService.isAuthor(workspace, user)) {
            throw new AccessDeniedException("Only author can change workspace properties");
        }
        return true;
    }
}
