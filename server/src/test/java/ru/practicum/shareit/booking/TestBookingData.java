package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;

import java.time.LocalDateTime;

public class TestBookingData {

    public static BookingDto getBookingDto() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.of(2025, 7, 25, 0, 0, 0));
        bookingDto.setEnd(LocalDateTime.of(2025, 7, 25, 1, 1, 1));
        bookingDto.setStatus(Status.APPROVED);
        bookingDto.setBooker(new BookingDto.Booker(1L));
        bookingDto.setItem(new BookingDto.BookingItem(1L, "item"));
        return bookingDto;
    }

    public static String getNewBookingJson() {
        return "{\n" +
               "  \"itemId\": \"1\",\n" +
               "  \"start\": \"2025-07-22T00:00:00\",\n" +
               "  \"end\": \"2025-07-23T00:00:00\"\n" +
               "}\n";
    }

    public static BookingRequest getBookingRequest() {
        BookingRequest request = new BookingRequest();
        request.setItemId(1L);
        request.setStart(LocalDateTime.now().plusDays(1));
        request.setEnd(LocalDateTime.now().plusDays(2));
        return request;
    }
}