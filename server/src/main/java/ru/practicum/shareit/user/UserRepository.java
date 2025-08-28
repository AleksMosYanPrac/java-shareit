package ru.practicum.shareit.user;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.dto.UserShort;

import java.util.Optional;

@Validated
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(long userId);

    Optional<User> findByEmail(@NotNull String email);

    void delete(User user);

    Optional<UserShort> getUserById(long userId);
}