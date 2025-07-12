package su.taskmanager.controller.workspace;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.user.repository.UserRepository;
import su.taskmanager.data.user.service.UserService;
import su.taskmanager.data.workspace.InvitationStatus;
import su.taskmanager.data.workspace.dto.create.InviteCreateDto;
import su.taskmanager.data.workspace.entity.Invite;
import su.taskmanager.data.workspace.entity.Workspace;
import su.taskmanager.data.workspace.repository.InviteRepository;
import su.taskmanager.data.workspace.repository.WorkspaceRepository;
import su.taskmanager.data.workspace.service.InviteService;
import su.taskmanager.data.workspace.service.WorkspaceService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/workspace/invite")
public class InviteController {
    private final InviteService inviteService;
    private final InviteRepository inviteRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final WorkspaceService workspaceService;
    private final WorkspaceRepository workspaceRepository;




    @GetMapping("/{uuid}")
    public ResponseEntity<?> getInvite(@PathVariable UUID uuid) {
        Optional<Invite> inviteOptional = inviteRepository.findByUuid(uuid);
        if (inviteOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Invite invite = inviteOptional.get();
        if (invite.getStatus() != InvitationStatus.PENDING) {
            return ResponseEntity.status(HttpStatus.GONE).body("Invitation expired");
        }
        return ResponseEntity.ok(invite);
    }
    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<?> inviteCreate(@RequestBody InviteCreateDto dto, @AuthenticationPrincipal User user) {
        Optional<Workspace> workspaceOptional = workspaceService.findById(dto.getWorkspace_id());
        Invite invite = new Invite();
        if (workspaceOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found workspace with id: "
                    + dto.getWorkspace_id());
        }
        Workspace workspace = workspaceOptional.get();
        if (!workspace.getAuthor().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only workspace author allowed to create invites");
        }
        invite.setSender(user);
        invite.setWorkspace(workspace);
        workspace.addInvite(invite);
        inviteService.createInvite(invite);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created invite with UUID: "
                + invite.getUuid());
    }


    @Transactional
    @ResponseBody
    @PostMapping("/{uuid}/accept")
    public ResponseEntity<?> acceptInvitation(@PathVariable UUID uuid, @AuthenticationPrincipal User user) {

    Optional<Invite> inviteOptional = inviteRepository.findByUuid(uuid);
    if (inviteOptional.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found invite by UUID: " + uuid);
    }
    Invite invite = inviteOptional.get();
    if (invite.getStatus() != InvitationStatus.PENDING) {
        return ResponseEntity.status(HttpStatus.GONE).body("Invitation expired");
    }
    if (invite.getWorkspace().getUsers().stream()
            .anyMatch(existingUser -> existingUser.getUsername().equals(user.getUsername()))) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("User already exists in the workspace");
    }

    workspaceService.addUserToWorkspace(invite.getWorkspace(), user);
    invite.setStatus(InvitationStatus.ACCEPTED);
    inviteRepository.save(invite);
    user.addWorkspace(invite.getWorkspace());
    userRepository.save(user);
    return ResponseEntity.ok("Accepted successfully");
    }

    @ResponseBody
    @PostMapping("/{uuid}/decline")
    public ResponseEntity<?> declineInvitation(@PathVariable UUID uuid, @AuthenticationPrincipal User user) {
        Optional<Invite> inviteOptional = inviteRepository.findByUuid(uuid);
        if (inviteOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Invite invite = inviteOptional.get();
        if (invite.getStatus() != InvitationStatus.PENDING) {
            return ResponseEntity.status(HttpStatus.GONE).body("Invite expired");
        }
        if (user != null) {
            invite.setStatus(InvitationStatus.DECLINED);
            inviteRepository.save(invite);
            return ResponseEntity.ok("Declined successfully");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User must be authenticated");
    }

}
