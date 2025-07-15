package ru.practicum.shareit.booking.exceptions;

public class BookingNotFound extends Exception {
    public BookingNotFound(long bookerId, long bookingId) {
        super("Booking with id:" + bookingId + " for user with id:" + bookerId + "not found");
    }
}