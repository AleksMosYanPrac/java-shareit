package ru.practicum.shareit.item.exceptions;

public class ItemNotFound extends Exception {
    public ItemNotFound(long itemId) {
        super("Item not found with ID: " + itemId);
    }

    public ItemNotFound(String message) {
        super(message);
    }
}