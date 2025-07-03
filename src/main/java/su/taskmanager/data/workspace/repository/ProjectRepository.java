package su.taskmanager.data.workspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import su.taskmanager.data.workspace.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
