package ru.practicum.shareit.item.interfaces;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.dto.CommentShort;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfo;

import java.util.List;

public interface ItemMapper {
    Item toItem(ItemDto itemDto);

    ItemDto toItemDto(Item item);

    ItemInfo toItemInfo(Item item, BookingShort lastBooking, BookingShort nextBooking, List<CommentShort> comments);
}