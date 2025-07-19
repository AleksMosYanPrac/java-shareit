package ru.practicum.shareit.request.interfaces;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfo;
import ru.practicum.shareit.request.exceptions.RequestNotFound;
import ru.practicum.shareit.user.exceptions.UserNotFound;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(long userId, ItemRequestDto requestDto) throws UserNotFound;

    List<ItemRequestDto> getUsersRequests(long userId) throws UserNotFound;

    ItemRequestInfo getRequestInfoById(long userId, long requestId) throws RequestNotFound;

    List<ItemRequestDto> getAllRequests();
}