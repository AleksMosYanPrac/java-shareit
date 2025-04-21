package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class User {

    Long id;

    @Email
    @NotNull
    String email;

    @NotBlank
    String name;
}