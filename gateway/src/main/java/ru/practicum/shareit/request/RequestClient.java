package ru.practicum.shareit.request;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;


public class RequestClient extends BaseClient {

    public RequestClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public ResponseEntity<Object> postRequest(long userId, ItemRequestDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getUserRequests(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getRequest(long userId, long requestId) {
        return get("/" + requestId, userId);
    }

    public ResponseEntity<Object> getAllRequests() {
        return get("/all");
    }
}