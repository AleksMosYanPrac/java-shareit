package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfo;
import ru.practicum.shareit.request.exceptions.RequestNotFound;
import ru.practicum.shareit.request.interfaces.ItemRequestMapper;
import ru.practicum.shareit.request.interfaces.ItemRequestService;
import ru.practicum.shareit.user.exceptions.UserNotFound;
import ru.practicum.shareit.user.interfaces.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserService userService;
    private final ItemRequestRepository repository;
    private final ItemRequestMapper mapper;

    @Override
    public ItemRequestDto addItemRequest(long userId, ItemRequestDto requestDto) throws UserNotFound {
        Long requesterId = userService.getUserById(userId).getId();
        ItemRequest newRequest = mapper.toItemRequest(requesterId, requestDto);
        newRequest.setCreated(LocalDateTime.now());
        return mapper.toDto(repository.save(newRequest));
    }

    @Override
    public List<ItemRequestDto> getUsersRequests(long userId) throws UserNotFound {
        Long requesterId = userService.getUserById(userId).getId();
        List<ItemRequest> requests = repository.findAllByRequesterIdOrderByCreatedDesc(requesterId);
        return requests.stream().map(mapper::toDto).toList();
    }

    @Override
    public ItemRequestInfo getRequestInfoById(long userId, long requestId) throws RequestNotFound {
        ItemRequest request = repository.findById(requestId).orElseThrow(() -> new RequestNotFound(requestId));
        return mapper.toItemRequestInfo(request);
    }

    @Override
    public List<ItemRequestDto> getAllRequests() {
        return repository.findAllByOrderByCreatedDesc().stream().map(mapper::toDto).toList();
    }
}