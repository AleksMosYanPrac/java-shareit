package ru.practicum.shareit.item.interfaces;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.Item;

import java.util.List;
import java.util.Optional;

@Validated
public interface ItemRepository {

    Item save(@Valid Item item);

    Optional<Item> getItemById(long itemId);

    List<Item> findAllOwnerItems(long ownerId);

    List<Item> findAllAvailableItemsByNameContains(String text);
}