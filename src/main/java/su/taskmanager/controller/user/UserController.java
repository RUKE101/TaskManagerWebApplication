package su.taskmanager.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import su.taskmanager.data.user.dto.read.UserGetDto;
import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.user.repository.UserRepository;
import su.taskmanager.data.user.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService,
                          UserRepository userRepository) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetDto> getUser(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(user -> {
                    UserGetDto dto = new UserGetDto();
                    dto.setUserId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setEmail(user.getEmail());
                    dto.setWorkspaces(user.getWorkspaces());
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

}
