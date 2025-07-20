package su.taskmanager.data.workspace.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.workspace.entity.Invite;
import su.taskmanager.data.workspace.entity.Workspace;
import su.taskmanager.data.workspace.repository.InviteRepository;

import java.util.UUID;

@Builder
@Service
public class InviteService {
    private final InviteRepository inviteRepository;
    private final WorkspaceService workspaceService;

    @Transactional
    public Invite createInvite(Workspace workspace, User user) {
        Invite invite = new Invite();
        invite.setSender(user);
        invite.setWorkspace(workspace);
        workspace.addInvite(invite);
        return inviteRepository.save(invite);
    }

    @Transactional
    public void save(Invite invite) {
        inviteRepository.save(invite);
    }

    @Transactional
    public void deleteInvite(UUID inviteUuid) {
        Invite invite = findInviteByUuid(inviteUuid);
        Workspace workspace = workspaceService.getWorkspace(invite.getWorkspace().getId());
        workspace.removeInvite(invite);
        workspaceService.save(workspace);
        inviteRepository.delete(invite);
    }


    public Invite findInviteByUuid(UUID uuid) {
        return inviteRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Not found invite with uuid: " + uuid));
    }
}
