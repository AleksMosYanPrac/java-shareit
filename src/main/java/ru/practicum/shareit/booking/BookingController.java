package ru.practicum.shareit.booking;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.exceptions.BookingNotAvailable;
import ru.practicum.shareit.booking.exceptions.BookingNotFound;
import ru.practicum.shareit.booking.interfaces.BookingService;
import ru.practicum.shareit.item.exceptions.ItemNotFound;
import ru.practicum.shareit.user.exceptions.UserNotFound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    BookingDto postBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                           @RequestBody BookingRequest bookingRequest)
            throws UserNotFound, ItemNotFound, BookingNotAvailable {
        return bookingService.addBooking(userId, bookingRequest);
    }

    @PatchMapping("/{bookingId}")
    BookingDto patchBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                            @PathVariable long bookingId,
                            @RequestParam boolean approved) throws UserNotFound, BookingNotFound {
        return bookingService.changeBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable long bookingId) throws UserNotFound, BookingNotFound {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    List<BookingDto> getBookerStatedBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestParam(defaultValue = "ALL") State state) throws UserNotFound {
        return bookingService.getBookingsForBooker(userId, state);
    }

    @GetMapping("/owner")
    List<BookingDto> getOwnerStatedBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestParam(defaultValue = "ALL") State state) throws UserNotFound {
        return bookingService.getBookingsForOwner(userId, state);
    }

    @ExceptionHandler(UserNotFound.class)
    ResponseEntity<Map<String, String>> onUserNotFound(UserNotFound exception) {
        Map<String, String> body = new HashMap<>();
        body.put("message", exception.getMessage());
        log.info("User not found: {}", body);
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ItemNotFound.class)
    ResponseEntity<Map<String, String>> onItemNotFound(ItemNotFound exception) {
        Map<String, String> body = new HashMap<>();
        body.put("message", exception.getMessage());
        log.info("Item not found: {}", body);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookingNotFound.class)
    ResponseEntity<Map<String, String>> onBookingNotFound(BookingNotFound exception) {
        Map<String, String> body = new HashMap<>();
        body.put("message", exception.getMessage());
        log.info("Booking not found: {}", body);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookingNotAvailable.class)
    ResponseEntity<Map<String, String>> onBookingNotAvailable(BookingNotAvailable exception) {
        Map<String, String> body = new HashMap<>();
        body.put("message", exception.getMessage());
        log.info("Booking not available: {}", body);
        return new ResponseEntity<>(body, BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Map<String, String>> onConstraintViolation(ConstraintViolationException exception) {
        Map<String, String> body = new HashMap<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            body.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        log.info("BeanValidation fail: {}", body);
        return new ResponseEntity<>(body, BAD_REQUEST);
    }
}