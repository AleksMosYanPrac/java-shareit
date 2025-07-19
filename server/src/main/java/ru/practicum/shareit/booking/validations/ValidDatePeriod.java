package ru.practicum.shareit.booking.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartDateIsBeforeEndDate.class)
public @interface ValidDatePeriod {

    String message() default "Start Date of 'time period' can't be after End Date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}