package ru.practicum.shareit.item;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

public class ItemClient extends BaseClient {

    public ItemClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    ResponseEntity<Object> postItem(long userId, ItemDto itemDto) {
        return post("", userId, itemDto);
    }

    ResponseEntity<Object> patchItem(long userId, long itemId, ItemDto itemDto) {
        return patch("/" + itemId, userId, itemDto);
    }

    ResponseEntity<Object> getItem(long itemId) {
        return get("/" + itemId);
    }

    ResponseEntity<Object> getItems(long userId) {
        return get("", userId);
    }

    ResponseEntity<Object> getItemsByNameContain(String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search", 0L, parameters);
    }

    ResponseEntity<Object> postComment(long userId, long itemId, CommentDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}