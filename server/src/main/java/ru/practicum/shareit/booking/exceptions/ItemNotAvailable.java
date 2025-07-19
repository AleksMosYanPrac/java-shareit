package ru.practicum.shareit.booking.exceptions;

public class ItemNotAvailable extends Exception {
    public ItemNotAvailable(Long id) {
        super(String.format("Item with ID: %d not available for booking", id));
    }
}