package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private Booker booker;
    private BookingItem item;

    @Data
    @AllArgsConstructor
    public static class Booker {
        private Long id;
    }

    @Data
    @AllArgsConstructor
    public static class BookingItem {
        private long id;
        private String name;
    }
}