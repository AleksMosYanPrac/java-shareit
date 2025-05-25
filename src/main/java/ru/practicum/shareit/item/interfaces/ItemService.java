package ru.practicum.shareit.item.interfaces;

import ru.practicum.shareit.item.exceptions.ItemNotFound;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.exceptions.UserNotFound;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(long userId, ItemDto item) throws UserNotFound;

    ItemDto updateItem(long userId, long itemId, ItemDto item) throws UserNotFound, ItemNotFound;

    ItemDto getItemById(long itemId) throws ItemNotFound;

    List<ItemDto> getUserItems(long userId) throws UserNotFound;

    List<ItemDto> getAvailableItemsByNameContains(String text);
}