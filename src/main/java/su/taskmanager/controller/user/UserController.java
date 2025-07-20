package su.taskmanager.controller.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import su.taskmanager.data.user.dto.read.UserUpdateDto;
import su.taskmanager.mappers.UserMapper;
import su.taskmanager.security.JwtTokenProvider;
import su.taskmanager.data.user.dto.LoginRequestDto;
import su.taskmanager.data.user.dto.read.UserDto;
import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.user.service.UserService;

@Builder
@RestController
@RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    @ResponseBody
    @GetMapping
    public ResponseEntity<?> getSelf(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserDtoById(user.getId()));

    }

    // DEBUG MAPPING/METHOD TODO REMOVE BEFORE PRODUCTION
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return userService.findDtoById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @AuthenticationPrincipal User user,
                                        @RequestBody UserUpdateDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(user, dto));
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
        UserDto dto = UserMapper.toDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (userService.isNameOccupied(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username with this username already exists");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }




}
