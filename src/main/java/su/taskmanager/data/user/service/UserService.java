package su.taskmanager.data.user.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import su.taskmanager.data.user.dto.read.UserGetDto;
import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.user.repository.UserRepository;
import su.taskmanager.mappers.UserMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<UserGetDto> findDtoById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDto);
    }

    public UserGetDto saveUser(User user) {

        User saved = userRepository.save(user);
        return UserMapper.toDto(saved);
    }

    public Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User authenticate(String username, String rawPassword) {
        Optional<User> userOptional = this.findByUsername(username);
        if (userOptional.isEmpty()) {
            return null;
        }

        User user = userOptional.get();

        if (rawPassword.equals(user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }
}
