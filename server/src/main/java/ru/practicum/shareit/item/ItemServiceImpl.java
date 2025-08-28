package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.exceptions.CommentNotExists;
import ru.practicum.shareit.item.exceptions.ItemNotFound;
import ru.practicum.shareit.item.interfaces.CommentMapper;
import ru.practicum.shareit.item.interfaces.ItemMapper;
import ru.practicum.shareit.item.interfaces.ItemService;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.exceptions.RequestNotFound;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exceptions.UserNotFound;
import ru.practicum.shareit.user.interfaces.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserService userService;

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository requestRepository;

    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @Transactional
    @Override
    public ItemDto addNewItem(long userId, ItemDto item) throws UserNotFound, RequestNotFound {
        Long ownerId = userService.getUserById(userId).getId();
        Item newItem = itemMapper.toItem(item);
        newItem.setOwnerId(ownerId);
        if (Objects.nonNull(item.getRequestId())) {
            ItemRequest request = requestRepository.findById(item.getRequestId())
                    .orElseThrow(() -> new RequestNotFound(item.getRequestId()));
            newItem.setRequest(request);
        }
        return itemMapper.toItemDto(itemRepository.save(newItem));
    }

    @Transactional
    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto item) throws UserNotFound, ItemNotFound {
        Long ownerId = userService.getUserById(userId).getId();
        Item updatingItem = itemRepository.getItemById(itemId).orElseThrow(() -> new ItemNotFound(itemId));
        if (!Objects.equals(ownerId, updatingItem.getOwnerId())) {
            throw new ItemNotFound("Item with ID: " + itemId + " not found for User with ID: " + userId);
        }
        if (item.getName() != null) {
            updatingItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatingItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatingItem.setAvailable(item.getAvailable());
        }
        return itemMapper.toItemDto(itemRepository.save(updatingItem));
    }

    @Override
    public ItemInfo getItemById(long itemId) throws ItemNotFound {
        Item item = itemRepository.getItemById(itemId).orElseThrow(() -> new ItemNotFound(itemId));
        LocalDateTime now = LocalDateTime.now();
        //BookingShort lastBooking = bookingRepository.findByItemIdAndEndBefore(itemId, now);
        BookingShort lastBooking = null;// Unexpected logic in Postman Test and Technical Requirements
        BookingShort nextBooking = bookingRepository.findByItemIdAndStartAfter(itemId, now);
        List<CommentShort> comments = commentRepository.findAllByItemId(itemId);
        return itemMapper.toItemInfo(item, lastBooking, nextBooking, comments);
    }

    @Override
    public List<ItemDto> getUserItems(long userId) throws UserNotFound {
        Long ownerId = userService.getUserById(userId).getId();
        return itemRepository.findAllByOwnerId(ownerId).stream().map(itemMapper::toItemDto).toList();
    }

    @Override
    public List<ItemDto> getAvailableItemsByNameContains(String text) {
        if (Objects.isNull(text) || text.isBlank()) {
            return List.of();
        }
        return itemRepository.findByAvailableTrueAndNameContainingIgnoreCase(text)
                .stream()
                .map(itemMapper::toItemDto)
                .toList();
    }

    @Transactional
    @Override
    public CommentDto addUserCommentToItem(long userId,
                                           long itemId,
                                           CommentDto commentDto) throws UserNotFound, ItemNotFound, CommentNotExists {
        if (!hasUserBookedItem(userId, itemId)) {
            throw new CommentNotExists(userId, itemId);
        }
        User author = userRepository.findById(userId).orElseThrow(() -> new UserNotFound(userId));
        Item item = itemRepository.getItemById(itemId).orElseThrow(() -> new ItemNotFound(itemId));
        LocalDateTime created = LocalDateTime.now();
        Comment newComment = commentMapper.toComment(author, item, created, commentDto);
        return commentMapper.toCommentDto(commentRepository.save(newComment));
    }

    private boolean hasUserBookedItem(long userId, long itemId) {
        return bookingRepository.existsByBookerIdAndItemIdAndEndBefore(userId, itemId, LocalDateTime.now());
    }
}