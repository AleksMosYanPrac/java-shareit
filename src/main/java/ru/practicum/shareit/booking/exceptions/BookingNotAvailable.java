package ru.practicum.shareit.booking.exceptions;

public class BookingNotAvailable extends Exception {
    public BookingNotAvailable(Long id) {
        super("Booking is not available for Item with ID: " + id);
    }
}