package ru.practicum.shareit.user.interfaces;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.User;

import java.util.Optional;

@Validated
public interface UserRepository {

    User save(@Valid User user);

    Optional<User> findById(long userId);

    void delete(User user);
}