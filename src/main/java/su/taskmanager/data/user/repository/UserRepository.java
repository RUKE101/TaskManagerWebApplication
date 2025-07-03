package su.taskmanager.data.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import su.taskmanager.data.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
