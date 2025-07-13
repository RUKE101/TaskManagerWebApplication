package su.taskmanager.controller.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
        if (!workspaceService.isAuthor(workspace, user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only workspace author allowed to create invites");
        }

        Invite invite  = inviteService.createInvite(workspace, user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created invite with UUID: "
                + invite.getUuid());
    }

    @Transactional
    @ResponseBody
    @PostMapping("/{uuid}/accept")
    public ResponseEntity<?> acceptInvitation(@PathVariable UUID uuid, @AuthenticationPrincipal User user) {
    Invite invite = inviteService.findInviteByUuid(uuid);
    isExpired(invite);
    if (invite.getWorkspace().getUsers().stream()
            .anyMatch(existingUser -> existingUser.getUsername().equals(user.getUsername()))) {
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

    public Boolean isExpired(Invite invite) {
        if (invite.getStatus() != InvitationStatus.PENDING) {
            throw new RuntimeException("Invite expired");
        }
        else return false;
    }
}
