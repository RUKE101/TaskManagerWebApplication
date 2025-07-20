package su.taskmanager.data.workspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import su.taskmanager.data.workspace.entity.Workspace;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    List<Workspace> findByAuthorId(Long userId);
    Optional<Workspace> findById(Long id);

}
