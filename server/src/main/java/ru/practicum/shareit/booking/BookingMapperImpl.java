package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.booking.interfaces.BookingMapper;
import ru.practicum.shareit.item.Item;

@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public Booking toBooking(User booker, Item item, BookingRequest bookingDto) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        return booking;
    }

    @Override
    public BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setBooker(new BookingDto.Booker(booking.getBooker().getId()));
        bookingDto.setItem(new BookingDto.BookingItem(booking.getItem().getId(),booking.getItem().getName()));
        return bookingDto;
    }
}