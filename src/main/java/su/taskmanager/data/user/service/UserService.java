package su.taskmanager.data.user.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.taskmanager.data.user.dto.read.UserGetDto;
import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.user.repository.UserRepository;
import su.taskmanager.mappers.UserMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserGetDto createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        return UserMapper.toDto(saved);
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    public Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public User getUserByUsername(String username) {
        Optional<User> optionalUser = findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("Not found user with username: " + username);
        }
        return optionalUser.get();
    }

    public User getUserById(Long id) {
        Optional<User> optionalUser = findUserById(id);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("Not found user with id: " + id);
        }
        return optionalUser.get();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<UserGetDto> findDtoById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDto);
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
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }

    public boolean isNameOccupied(String username) {
        return findByUsername(username).isPresent();
    }


}
