package ru.practicum.shareit.user.exceptions;

public class UserExists extends Exception {
    public UserExists() {
        super("User already exists");
    }
}