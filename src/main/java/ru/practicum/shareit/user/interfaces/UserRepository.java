package ru.practicum.shareit.user.interfaces;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.User;

import java.util.Optional;

@Validated
public interface UserRepository {

    User save(@Valid User user);

    Optional<User> findById(long userId);

    Optional<User> findByEmail(@NotNull String email);

    void delete(User user);
}