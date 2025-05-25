package ru.practicum.shareit.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exceptions.UserExists;
import ru.practicum.shareit.user.exceptions.UserNotFound;
import ru.practicum.shareit.user.interfaces.UserService;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    UserDto postUser(@RequestBody UserDto user) throws UserExists {
        return userService.addNewUser(user);
    }

    @PatchMapping("/{userId}")
    UserDto patchUser(@PathVariable long userId, @RequestBody UserDto userData) throws UserNotFound, UserExists {
        return userService.updateUser(userId, userData);
    }

    @GetMapping("/{userId}")
    UserDto getUser(@PathVariable long userId) throws UserNotFound {
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable long userId) throws UserNotFound {
        userService.deleteUserById(userId);
    }

    @ExceptionHandler(UserExists.class)
    ResponseEntity<Map<String, String>> onUserExists(UserExists e) {
        Map<String, String> body = new HashMap<>();
        body.put("message", e.getMessage());
        log.info("User Exists: {}", body);
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFound.class)
    ResponseEntity<Map<String, String>> onUserNotFound(UserNotFound e) {
        Map<String, String> body = new HashMap<>();
        body.put("message", e.getMessage());
        log.info("User not found: {}", body);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Map<String, String>> onConstraintViolation(ConstraintViolationException exception) {
        Map<String, String> body = new HashMap<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            body.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        log.info("BeanValidation fail: {}", body);
        return new ResponseEntity<>(body, BAD_REQUEST);
    }
}