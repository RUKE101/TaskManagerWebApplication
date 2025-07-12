package su.taskmanager.controller.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import su.taskmanager.controller.security.JwtTokenProvider;
import su.taskmanager.data.user.dto.LoginRequestDto;
import su.taskmanager.data.user.dto.read.UserGetDto;
import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.user.service.UserService;
import su.taskmanager.mappers.UserMapper;

@Builder
@RestController
@RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @ResponseBody
    @GetMapping
    public ResponseEntity<?> getSelf(@AuthenticationPrincipal User user) {
        return userService.findDtoById(user.getId())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }


    // DEBUG MAPPING/METHOD TODO REMOVE BEFORE PRODUCTION
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
        cookie.setSecure(true); // Для https
        cookie.setPath("/");
        cookie.setMaxAge(3600000);
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
