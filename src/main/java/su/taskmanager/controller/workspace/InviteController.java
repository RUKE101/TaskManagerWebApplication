package su.taskmanager.controller.workspace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


@Builder
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
            return ResponseEntity.status(HttpStatus.GONE).body("Приглашение уже неактуально");
        }
        return ResponseEntity.ok(invite);
    }
    @PostMapping("/create")
    public Object inviteCreate(@RequestBody InviteCreateDto dto) {
        System.out.println("SENDER " + dto.getSender_id());
        Optional<User> senderOptional = userRepository.findById(dto.getSender_id());
        if (senderOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Optional<Workspace> workspaceOptional = workspaceService.findById(dto.getWorkspace_id());
        if (senderOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Invite invite = new Invite();
        Workspace workspace = workspaceOptional.get();
        User sender = senderOptional.get();
        invite.setSender(sender);
        invite.setWorkspace(workspace);




        workspace.addInvite(invite);
        return inviteService.createInvite(invite);
    }



    @PostMapping("/{uuid}/accept")
    public ResponseEntity<?> acceptInvitation(@PathVariable UUID uuid) { // , @AuthenticationPrincipal User user) {
        Long id = Long.parseLong("1");
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.get();
    Optional<Invite> inviteOptional = inviteRepository.findByUuid(uuid);
    if (inviteOptional.isEmpty()) {
        return ResponseEntity.notFound().build();
    }
    Invite invite = inviteOptional.get();
    if (invite.getStatus() != InvitationStatus.PENDING) {
        return ResponseEntity.status(HttpStatus.GONE).body("Приглашение уже неактуально");
    }
    System.out.print("WORKSPACE: "+invite.getWorkspace());
    workspaceService.addUserToWorkspace(invite.getWorkspace(), user);
    invite.setStatus(InvitationStatus.ACCEPTED);
    inviteRepository.save(invite);
    user.addWorkspace(invite.getWorkspace());
    userRepository.save(user);
    return ResponseEntity.ok("Принято");
    }

    @PostMapping("/{uuid}/decline")
    public ResponseEntity<?> declineInvitation(@PathVariable UUID uuid) {
        Optional<Invite> inviteOptional = inviteRepository.findByUuid(uuid);
        if (inviteOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Invite invite = inviteOptional.get();
        if (invite.getStatus() != InvitationStatus.PENDING) {
            return ResponseEntity.status(HttpStatus.GONE).body("Приглашение уже неактуально");
        }
        invite.setStatus(InvitationStatus.DECLINED);
        inviteRepository.save(invite);
        return ResponseEntity.ok("Принято");
    }


}
