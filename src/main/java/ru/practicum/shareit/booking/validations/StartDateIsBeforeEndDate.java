package ru.practicum.shareit.booking.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingRequest;

import java.util.Objects;

public class StartDateIsBeforeEndDate implements ConstraintValidator<ValidDatePeriod, BookingRequest> {

    @Override
    public void initialize(ValidDatePeriod constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BookingRequest bookingRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(bookingRequest.getStart()) || Objects.isNull(bookingRequest.getEnd())) {
            return false;
        }
        return bookingRequest.getStart().isBefore(bookingRequest.getEnd());
    }
}