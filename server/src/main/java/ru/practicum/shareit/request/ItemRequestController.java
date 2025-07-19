package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfo;
import ru.practicum.shareit.request.exceptions.RequestNotFound;
import ru.practicum.shareit.request.interfaces.ItemRequestService;
import ru.practicum.shareit.user.exceptions.UserNotFound;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDto postRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @RequestBody ItemRequestDto requestDto) throws UserNotFound {
        return requestService.addItemRequest(userId, requestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getUserOwnRequests(@RequestHeader("X-Sharer-User-Id") long userId) throws UserNotFound {
        return requestService.getUsersRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestInfo getRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable long requestId) throws RequestNotFound {
        return requestService.getRequestInfoById(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getRequests() {
        return requestService.getAllRequests();
    }
}