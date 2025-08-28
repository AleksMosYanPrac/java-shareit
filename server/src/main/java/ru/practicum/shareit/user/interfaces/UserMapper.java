package ru.practicum.shareit.user.interfaces;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.User;

public interface UserMapper {
    User toUser(UserDto user);

    UserDto toUserDto(User user);
}