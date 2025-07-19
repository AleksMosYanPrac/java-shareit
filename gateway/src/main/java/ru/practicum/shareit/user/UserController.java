package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@Validated
@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> postUser(@RequestBody @Valid UserDto user) {
        log.info("Adding new User {}, user name={}", user, user.getName());
        return userClient.postUser(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> patchUser(@PathVariable long userId, @RequestBody UserDto user) {
        log.info("Patching User with ID: {}, user And name={}", userId, user.getName());
        return userClient.patchUser(userId, user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable long userId) {
        log.info("Get User with ID: {}", userId);
        return userClient.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable long userId) {
        log.info("Delete User with ID: {}", userId);
        return userClient.deleteUser(userId);
    }
}