package ru.practicum.shareit.booking;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.exceptions.BookingNotAvailable;
import ru.practicum.shareit.booking.exceptions.BookingNotFound;
import ru.practicum.shareit.booking.interfaces.BookingMapper;
import ru.practicum.shareit.booking.interfaces.BookingService;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.exceptions.ItemNotFound;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserShort;
import ru.practicum.shareit.user.exceptions.UserNotFound;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper mapper;

    @Transactional
    @Override
    public BookingDto addBooking(long userId,
                                 BookingRequest bookingDto) throws UserNotFound, ItemNotFound, BookingNotAvailable {
        User booker = userRepository.findById(userId).orElseThrow(() -> new UserNotFound(userId));
        Item item = itemRepository
                .getItemById(bookingDto.getItemId())
                .orElseThrow(() -> new ItemNotFound(bookingDto.getItemId()));
        if (!item.isAvailable()) {
            throw new BookingNotAvailable(item.getId());
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
        UserShort owner = userRepository.getUserById(userId)
                .orElseThrow(() -> new UserNotFound(userId));
        Booking booking = bookingRepository
                .getBookingByIdAndItemOwnerId(bookingId, owner.getId())
                .orElseThrow(() -> new BookingNotFound(owner.getId(), bookingId));
        booking.changeStatus(isApproved);
        return mapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(long userId, long bookingId) throws UserNotFound, BookingNotFound {
        Long ownerOrBooker = userRepository.getUserById(userId)
                .map(UserShort::getId)
                .orElseThrow(() -> new UserNotFound(userId));
        Booking booking = bookingRepository
                .getBookingByBookerIdOrItemOwnerIdAndId(ownerOrBooker, ownerOrBooker, bookingId)
                .orElseThrow(() -> new BookingNotFound(ownerOrBooker, bookingId));
        return mapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsForBooker(long userId, State state) throws UserNotFound {
        UserShort booker = userRepository.getUserById(userId).orElseThrow(() -> new UserNotFound(userId));
        List<Booking> list = new ArrayList<>();
        BooleanExpression byBookerId = QBooking.booking.booker.id.eq(booker.getId());
        BooleanExpression expression = getExpressionCaseState(state);
        bookingRepository.findAll(byBookerId.and(expression)).forEach(list::add);
        return list.stream()
                .sorted(Comparator.comparing(Booking::getStart))
                .map(mapper::toBookingDto)
                .toList();
    }

    @Override
    public List<BookingDto> getBookingsForOwner(long userId, State state) throws UserNotFound {
        Long ownerId = userRepository.getUserById(userId)
                .map(UserShort::getId)
                .orElseThrow(() -> new UserNotFound(userId));
        List<Booking> list = new ArrayList<>();
        BooleanExpression byOwnerId = QBooking.booking.item.ownerId.eq(ownerId);
        BooleanExpression expression = getExpressionCaseState(state);
        bookingRepository.findAll(byOwnerId.and(expression)).forEach(list::add);
        return list.stream()
                .sorted(Comparator.comparing(Booking::getStart))
                .map(mapper::toBookingDto)
                .toList();
    }

    private BooleanExpression getExpressionCaseState(State state) {
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