package ru.practicum.shareit.booking;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingRequest;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class BookingRequestValidationTest {

    @Autowired
    private Validator validator;

    @Test
    void shouldValidateWhenStartDateIsBeforeEndDate() {
        BookingRequest request = new BookingRequest();
        request.setStart(LocalDateTime.now());
        request.setEnd(LocalDateTime.now().plusDays(1L));
        request.setItemId(1L);

        Set<ConstraintViolation<BookingRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldNotValidateWhenStartDateIsAfterEndDate() {
        BookingRequest request = new BookingRequest();
        request.setStart(LocalDateTime.now().plusDays(2L));
        request.setEnd(LocalDateTime.now().plusDays(1L));
        request.setItemId(1L);

        Set<ConstraintViolation<BookingRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .contains("Start Date of 'time period' can't be after End Date");
    }

    @Test
    void shouldNotValidateWhenRequestItemIdIsNull() {
        BookingRequest request = new BookingRequest();
        request.setItemId(null);
        request.setStart(LocalDateTime.now());
        request.setEnd(LocalDateTime.now().plusDays(1L));

        Set<ConstraintViolation<BookingRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .contains("must not be null");
    }

    @Test
    void shouldNotValidateWhenEndDateIsInPast() {
        BookingRequest request = new BookingRequest();
        request.setStart(LocalDateTime.now().minusDays(2L));
        request.setEnd(LocalDateTime.now().minusDays(1L));
        request.setItemId(1L);

        Set<ConstraintViolation<BookingRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .contains("must be a future date");
    }
}