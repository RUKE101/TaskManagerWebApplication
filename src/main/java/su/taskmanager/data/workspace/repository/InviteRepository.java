package su.taskmanager.data.workspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import su.taskmanager.data.workspace.entity.Invite;

import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, Long> {
    Optional<Invite> findByUuid(UUID uuid);
}
