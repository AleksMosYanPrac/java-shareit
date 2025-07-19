package ru.practicum.shareit.item.exceptions;

public class CommentNotExists extends Exception {
    public CommentNotExists(long userId, long itemId) {
        super(String.format("Commenting not exists for User with ID: %d for Item with ID: %d", userId, itemId));
    }
}