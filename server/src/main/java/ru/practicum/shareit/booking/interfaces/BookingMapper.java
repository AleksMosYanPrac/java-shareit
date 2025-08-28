package ru.practicum.shareit.booking.interfaces;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

public interface BookingMapper {

    Booking toBooking(User booker, Item item, BookingRequest bookingDto);

    BookingDto toBookingDto(Booking booking);
}