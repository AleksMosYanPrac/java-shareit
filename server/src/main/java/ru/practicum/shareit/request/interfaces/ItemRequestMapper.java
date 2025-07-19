package ru.practicum.shareit.request.interfaces;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfo;

public interface ItemRequestMapper {

    ItemRequest toItemRequest(Long requesterId, ItemRequestDto dto);

    ItemRequestDto toDto(ItemRequest request);

    ItemRequestInfo toItemRequestInfo(ItemRequest request);
}