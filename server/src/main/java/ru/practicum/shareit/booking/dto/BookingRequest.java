package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.booking.validations.ValidDatePeriod;

import java.time.LocalDateTime;

@Data
@ValidDatePeriod
public class BookingRequest {

    @NotNull
    private Long itemId;

    @NotNull
    private LocalDateTime start;

    @Future
    private LocalDateTime end;
}