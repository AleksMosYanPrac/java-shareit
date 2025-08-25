package ru.practicum.shareit.booking;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.exceptions.ItemNotAvailable;
import ru.practicum.shareit.booking.exceptions.BookingNotFound;
import ru.practicum.shareit.booking.interfaces.BookingMapper;
import ru.practicum.shareit.booking.interfaces.BookingService;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.exceptions.ItemNotFound;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exceptions.UserNotFound;
import ru.practicum.shareit.user.interfaces.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper mapper;

    @Transactional
    @Override
    public BookingDto addBooking(long userId,
                                 BookingRequest bookingDto) throws UserNotFound, ItemNotFound, ItemNotAvailable {
        User booker = userRepository.findById(userId).orElseThrow(() -> new UserNotFound(userId));
        Item item = itemRepository
                .getItemById(bookingDto.getItemId())
                .orElseThrow(() -> new ItemNotFound(bookingDto.getItemId()));
        if (!item.isAvailable()) {
            throw new ItemNotAvailable(item.getId());
        }
        Booking newBooking = mapper.toBooking(booker, item, bookingDto);
        newBooking.setStatus(Status.WAITING);
        return mapper.toBookingDto(bookingRepository.save(newBooking));
    }

    @Transactional
    @Override
    public BookingDto changeBookingStatus(long userId,
                                          long bookingId,
                                          boolean isApproved) throws UserNotFound, BookingNotFound {
        Long ownerId = userService.getUserById(userId).getId();
        Booking booking = bookingRepository
                .getBookingByIdAndItemOwnerId(bookingId, ownerId)
                .orElseThrow(() -> new BookingNotFound(ownerId, bookingId));
        booking.changeStatus(isApproved);
        return mapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(long userId, long bookingId) throws UserNotFound, BookingNotFound {
        Long ownerOrBooker = userService.getUserById(userId).getId();
        Booking booking = bookingRepository
                .getBookingByIdAndBookerId(bookingId, ownerOrBooker)
                .orElse(bookingRepository.getBookingByIdAndItemOwnerId(bookingId, ownerOrBooker)
                        .orElse(null));
        if (Objects.isNull(booking)) {
            throw new BookingNotFound(ownerOrBooker, bookingId);
        }
        return mapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsForBooker(long userId, BookingState state) throws UserNotFound {
        Long bookerId = userService.getUserById(userId).getId();
        List<BookingDto> list = new ArrayList<>();
        BooleanExpression byBookerId = QBooking.booking.booker.id.eq(bookerId);
        BooleanExpression expression = getExpressionCaseState(state);
        bookingRepository.findAll(byBookerId.and(expression), QBooking.booking.start.asc())
                .forEach(b -> list.add(mapper.toBookingDto(b)));
        return list;
    }

    @Override
    public List<BookingDto> getBookingsForOwner(long userId, BookingState state) throws UserNotFound {
        Long ownerId = userService.getUserById(userId).getId();
        List<BookingDto> list = new ArrayList<>();
        BooleanExpression byOwnerId = QBooking.booking.item.ownerId.eq(ownerId);
        BooleanExpression expression = getExpressionCaseState(state);
        bookingRepository.findAll(byOwnerId.and(expression), QBooking.booking.start.asc())
                .forEach(b -> list.add(mapper.toBookingDto(b)));
        return list;
    }

    private BooleanExpression getExpressionCaseState(BookingState state) {
        LocalDateTime now = LocalDateTime.now();
        return switch (state) {
            case ALL -> null;
            case CURRENT -> QBooking.booking.start.before(now).and(QBooking.booking.end.after(now));
            case PAST -> QBooking.booking.end.before(now);
            case FUTURE -> QBooking.booking.start.after(now);
            case REJECTED -> QBooking.booking.status.eq(Status.REJECTED);
            default -> null;
        };
    }
}