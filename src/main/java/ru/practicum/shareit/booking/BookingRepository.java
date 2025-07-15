package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.booking.dto.BookingShort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking> {

    Optional<Booking> getBookingByBookerIdOrItemOwnerIdAndId(long bookerId, long ownerId, long bookingId);

    Optional<Booking> getBookingByIdAndItemOwnerId(long bookingId, long ownerId);

    BookingShort findByItemIdAndEndBefore(long itemId, LocalDateTime now);

    BookingShort findByItemIdAndStartAfter(long itemId, LocalDateTime now);

    List<BookingShort> findAllByBookerIdAndEndBefore(long bookerId, LocalDateTime now);

    boolean existsByBookerIdAndItemIdAndEndBefore(long bookerId, long itemId,LocalDateTime now);
}