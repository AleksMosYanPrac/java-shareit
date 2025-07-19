package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Slf4j
@Validated
@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> postRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestBody @Valid ItemRequestDto requestDto) {
        log.info("Adding new Request with description: {}", requestDto.getDescription());
        return requestClient.postRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get requests own by User with ID: {}", userId);
        return requestClient.getUserRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long requestId) {
        log.info("Get Request with ID: {}", requestId);
        return requestClient.getRequest(userId, requestId);
    }

    @GetMapping("all")
    public ResponseEntity<Object> getRequests() {
        log.info("Get ALL Requests");
        return requestClient.getAllRequests();
    }
}