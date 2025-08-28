package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.booking.validations.ValidDatePeriod;

import java.time.LocalDateTime;

@Data
@ValidDatePeriod
public class BookingRequest {

    @NotNull(message = "must not be null")
    private Long itemId;

    @NotNull(message = "must not be null")
    private LocalDateTime start;

    @Future(message = "must be a future date")
    private LocalDateTime end;
}