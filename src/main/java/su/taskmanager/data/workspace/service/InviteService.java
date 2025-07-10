package su.taskmanager.data.workspace.service;

import lombok.Builder;
import org.springframework.stereotype.Service;
import su.taskmanager.data.workspace.entity.Invite;
import su.taskmanager.data.workspace.repository.InviteRepository;

@Builder
@Service
public class InviteService {
    private final InviteRepository inviteRepository;


    public Invite createInvite(Invite invite) {
        return inviteRepository.save(invite);
    }
}
