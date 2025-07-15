package ru.practicum.shareit.booking.interfaces;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.exceptions.ItemNotAvailable;
import ru.practicum.shareit.booking.exceptions.BookingNotFound;
import ru.practicum.shareit.item.exceptions.ItemNotFound;
import ru.practicum.shareit.user.exceptions.UserNotFound;

import java.util.List;

@Validated
public interface BookingService {

    BookingDto addBooking(long userId,
                          @Valid BookingRequest bookingRequest) throws UserNotFound, ItemNotFound, ItemNotAvailable;

    BookingDto changeBookingStatus(long userId, long bookingId, boolean approved) throws UserNotFound, BookingNotFound;

    BookingDto getBookingById(long userId, long bookingId) throws UserNotFound, BookingNotFound;

    List<BookingDto> getBookingsForBooker(long userId, State state) throws UserNotFound;

    List<BookingDto> getBookingsForOwner(long userId, State state) throws UserNotFound;
}