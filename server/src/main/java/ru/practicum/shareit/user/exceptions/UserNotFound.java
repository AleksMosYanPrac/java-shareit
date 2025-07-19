package ru.practicum.shareit.user.exceptions;

public class UserNotFound extends Exception {
    public UserNotFound(long userId) {
        super("User not found with ID: " + userId);
    }
}