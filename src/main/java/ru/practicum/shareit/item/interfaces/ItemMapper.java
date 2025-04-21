package ru.practicum.shareit.item.interfaces;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;

public interface ItemMapper {
    Item toItem(ItemDto itemDto);

    ItemDto toItemDto(Item item);
}
