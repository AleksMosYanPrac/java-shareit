package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exceptions.ItemNotFound;
import ru.practicum.shareit.item.interfaces.ItemMapper;
import ru.practicum.shareit.item.interfaces.ItemRepository;
import ru.practicum.shareit.item.interfaces.ItemService;
import ru.practicum.shareit.user.exceptions.UserNotFound;
import ru.practicum.shareit.user.interfaces.UserService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper mapper;

    @Override
    public ItemDto addNewItem(long userId, ItemDto item) throws UserNotFound {
        Long ownerId = userService.getUserById(userId).getId();
        Item newItem = mapper.toItem(item);
        newItem.setOwnerId(ownerId);
        return mapper.toItemDto(itemRepository.save(newItem));
    }

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
        return mapper.toItemDto(itemRepository.save(updatingItem));
    }

    @Override
    public ItemDto getItemById(long itemId) throws ItemNotFound {
        Item item = itemRepository.getItemById(itemId).orElseThrow(() -> new ItemNotFound(itemId));
        return mapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getUserItems(long userId) throws UserNotFound {
        Long ownerId = userService.getUserById(userId).getId();
        return itemRepository.findAllOwnerItems(ownerId).stream().map(mapper::toItemDto).toList();
    }

    @Override
    public List<ItemDto> getAvailableItemsByNameContains(String text) {
        return itemRepository.findAllAvailableItemsByNameContains(text).stream().map(mapper::toItemDto).toList();
    }
}