package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.exceptions.BookingNotFound;
import ru.practicum.shareit.booking.exceptions.ItemNotAvailable;
import ru.practicum.shareit.booking.interfaces.BookingService;
import ru.practicum.shareit.item.exceptions.ItemNotFound;
import ru.practicum.shareit.user.exceptions.UserNotFound;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceTest {

    private final EntityManager em;
    private final BookingService bookingService;

    private long userId = 1L;
    private long bookingId = 1L;
    private long itemId = 1L;

    @Test
    @Sql(value = "/db/h2/tests/add_users_and_item.sql")
    void shouldAddNewBooking() throws Exception {
        BookingRequest request = TestBookingData.getBookingRequest();
        bookingService.addBooking(userId, request);

        TypedQuery<Booking> query = em.createQuery("select b from Booking b where b.item.id = :item_id", Booking.class);
        Booking booking = query.setParameter("item_id", request.getItemId()).getSingleResult();

        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getItem().getId(), equalTo(request.getItemId()));
    }

    @Test
    @Sql(value = "/db/h2/tests/add_users_and_item.sql")
    void shouldThrowItemNotAvailableOnAddNewBooking() throws Exception {
        BookingRequest request = TestBookingData.getBookingRequest();
        request.setItemId(3L);

        assertThrows(ItemNotAvailable.class, () -> bookingService.addBooking(userId, request));
    }

    @Test
    void shouldThrowUserNotFoundOnAddNewBooking() throws Exception {
        BookingRequest request = TestBookingData.getBookingRequest();

        assertThrows(UserNotFound.class, () -> bookingService.addBooking(userId, request));
    }

    @Test
    @Sql(value = "/db/h2/tests/add_users.sql")
    void shouldThrowItemNotFoundOnAddNewBooking() throws Exception {
        BookingRequest request = TestBookingData.getBookingRequest();

        assertThrows(ItemNotFound.class, () -> bookingService.addBooking(userId, request));
    }


    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    @Sql("/db/h2/tests/add_booking.sql")
    void shouldChangeBookingStatusToApproved() throws Exception {
        bookingService.changeBookingStatus(userId, bookingId, true);

        TypedQuery<Booking> query = em.createQuery("select b from Booking b where b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", bookingId).getSingleResult();

        assertThat(booking.getStatus(), equalTo(Status.APPROVED));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    @Sql("/db/h2/tests/add_booking.sql")
    void shouldChangeBookingStatusToRejected() throws Exception {
        bookingService.changeBookingStatus(userId, bookingId, false);

        TypedQuery<Booking> query = em.createQuery("select b from Booking b where b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", bookingId).getSingleResult();

        assertThat(booking.getStatus(), equalTo(Status.REJECTED));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    @Sql("/db/h2/tests/add_booking.sql")
    void shouldNotChangeBookingStatusToRejectedWhenItsAlreadyRejected() throws Exception {
        bookingService.changeBookingStatus(userId, bookingId, false);

        bookingService.changeBookingStatus(userId, bookingId, false);
        TypedQuery<Booking> query = em.createQuery("select b from Booking b where b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", bookingId).getSingleResult();

        assertThat(booking.getStatus(), equalTo(Status.REJECTED));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    @Sql("/db/h2/tests/add_booking.sql")
    void shouldGetBookingById() throws Exception {
        BookingDto booking = bookingService.getBookingById(userId, bookingId);

        assertThat(booking, notNullValue());
        assertThat(booking.getId(), equalTo(bookingId));
        assertThat(booking.getStatus(), equalTo(Status.WAITING));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    void shouldThrowBookingNotFoundOnGetBookingById() throws Exception {

        assertThrows(BookingNotFound.class, () -> bookingService.getBookingById(userId, bookingId));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    @Sql("/db/h2/tests/add_booking.sql")
    void shouldGetBookingsForBookerByStateIsAll() throws Exception {
        List<BookingDto> bookings = bookingService.getBookingsForBooker(userId, BookingState.ALL);

        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    void shouldGetBookingsForBookerByStateIsCurrent() throws Exception {
        BookingRequest request = TestBookingData.getBookingRequest();
        request.setStart(LocalDateTime.now());
        request.setEnd(LocalDateTime.now().plusDays(1));
        bookingService.addBooking(userId, request);

        List<BookingDto> bookings = bookingService.getBookingsForBooker(userId, BookingState.CURRENT);

        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.getFirst().getStart().isBefore(LocalDateTime.now()), equalTo(true));
        assertThat(bookings.getFirst().getEnd().isAfter(LocalDateTime.now()), equalTo(true));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    void shouldGetBookingsForBookerByStateIsFuture() throws Exception {
        BookingRequest request = TestBookingData.getBookingRequest();
        request.setStart(LocalDateTime.now().plusDays(1));
        request.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.addBooking(userId, request);

        List<BookingDto> bookings = bookingService.getBookingsForBooker(userId, BookingState.FUTURE);

        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.getFirst().getStart().isAfter(LocalDateTime.now()), equalTo(true));
        assertThat(bookings.getFirst().getEnd().isAfter(LocalDateTime.now()), equalTo(true));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    @Sql("/db/h2/tests/add_booking.sql")
    void shouldGetBookingsForBookerByStateIsPast() throws Exception {
        long userId = 2;

        List<BookingDto> bookings = bookingService.getBookingsForBooker(userId, BookingState.PAST);

        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.getFirst().getStart().isBefore(LocalDateTime.now()), equalTo(true));
        assertThat(bookings.getFirst().getEnd().isBefore(LocalDateTime.now()), equalTo(true));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    @Sql("/db/h2/tests/add_booking.sql")
    void shouldGetBookingsForBookerByStateIsWaiting() throws Exception {
        List<BookingDto> bookings = bookingService.getBookingsForBooker(userId, BookingState.WAITING);

        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.getFirst().getStatus(), equalTo(Status.WAITING));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    @Sql("/db/h2/tests/add_booking.sql")
    void getBookingsForOwner() throws Exception {
        List<BookingDto> bookings = bookingService.getBookingsForOwner(userId, BookingState.ALL);

        assertThat(bookings.size(), equalTo(3));
    }
}