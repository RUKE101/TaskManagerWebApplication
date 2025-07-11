package su.taskmanager.controller.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import su.taskmanager.controller.security.JwtTokenProvider;
import su.taskmanager.data.user.dto.LoginRequestDto;
import su.taskmanager.data.user.dto.read.UserGetDto;
import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.user.repository.UserRepository;
import su.taskmanager.data.user.service.UserService;
@Builder
@RestController
@RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;




    @GetMapping("/{id}")
    public ResponseEntity<UserGetDto> getUser(@PathVariable Long id) {
        return userService.findDtoById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest, HttpServletResponse response) {
        User user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        String token = jwtTokenProvider.createToken(user.getId(), user.getUsername());

        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // если используете HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(3600); // время жизни в секундах
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged in");
    }


    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("User with this username already exists");
        }
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created user");
    }




}
