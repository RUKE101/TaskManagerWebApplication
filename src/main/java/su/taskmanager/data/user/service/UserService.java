package su.taskmanager.data.user.service;

import org.springframework.stereotype.Service;
import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.user.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
