package ru.practicum.shareit.user.interfaces;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exceptions.UserExists;
import ru.practicum.shareit.user.exceptions.UserNotFound;

public interface UserService {

    UserDto addNewUser(UserDto user) throws UserExists;

    UserDto updateUser(long userId, UserDto userData) throws UserNotFound, UserExists;

    UserDto getUserById(long userId) throws UserNotFound;

    void deleteUserById(long userId) throws UserNotFound;
}