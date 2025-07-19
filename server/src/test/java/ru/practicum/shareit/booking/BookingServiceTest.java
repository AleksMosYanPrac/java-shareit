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
import ru.practicum.shareit.booking.interfaces.BookingService;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

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
    void addBooking() throws Exception {
        BookingRequest request = TestBookingData.getBookingRequest();
        bookingService.addBooking(userId, request);

        TypedQuery<Booking> query = em.createQuery("select b from Booking b where b.item.id = :item_id", Booking.class);
        Booking booking = query.setParameter("item_id", request.getItemId()).getSingleResult();

        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getItem().getId(), equalTo(request.getItemId()));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    @Sql("/db/h2/tests/add_booking.sql")
    void changeBookingStatus() throws Exception {
        bookingService.changeBookingStatus(userId, bookingId, true);

        TypedQuery<Booking> query = em.createQuery("select b from Booking b where b.item.id = :item_id", Booking.class);
        Booking booking = query.setParameter("item_id", itemId).getSingleResult();

        assertThat(booking.getStatus(), equalTo(Status.APPROVED));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    @Sql("/db/h2/tests/add_booking.sql")
    void getBookingById() throws Exception {
        BookingDto booking = bookingService.getBookingById(userId, bookingId);

        assertThat(booking, notNullValue());
        assertThat(booking.getId(), equalTo(bookingId));
        assertThat(booking.getStatus(), equalTo(Status.WAITING));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    @Sql("/db/h2/tests/add_booking.sql")
    void getBookingsForBooker() throws Exception {
        List<BookingDto> bookings = bookingService.getBookingsForBooker(userId, BookingState.ALL);

        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    @Sql("/db/h2/tests/add_booking.sql")
    void getBookingsForOwner() throws Exception {
        List<BookingDto> bookings = bookingService.getBookingsForOwner(userId, BookingState.ALL);

        assertThat(bookings.size(), equalTo(1));
    }
}