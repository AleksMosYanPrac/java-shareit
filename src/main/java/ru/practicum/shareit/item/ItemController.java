package ru.practicum.shareit.item;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfo;
import ru.practicum.shareit.item.exceptions.CommentNotExists;
import ru.practicum.shareit.item.exceptions.ItemNotFound;
import ru.practicum.shareit.item.interfaces.ItemService;
import ru.practicum.shareit.user.exceptions.UserNotFound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    ItemDto postItem(@RequestHeader("X-Sharer-User-Id") long userId,
                     @RequestBody ItemDto item) throws UserNotFound {
        return itemService.addNewItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    ItemDto patchItem(@RequestHeader("X-Sharer-User-Id") long userId,
                      @PathVariable long itemId,
                      @RequestBody ItemDto item) throws UserNotFound, ItemNotFound {

        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    ItemInfo getItem(@PathVariable long itemId) throws ItemNotFound {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") long userId) throws UserNotFound {
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    List<ItemDto> getSearchItems(@RequestParam @NotNull String text) {
        return itemService.getAvailableItemsByNameContains(text);
    }

    @PostMapping("/{itemId}/comment")
    CommentDto postCommentToItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable long itemId,
                                 @RequestBody CommentDto commentDto)
            throws UserNotFound, ItemNotFound, CommentNotExists {
        return itemService.addUserCommentToItem(userId, itemId, commentDto);
    }

    @ExceptionHandler(UserNotFound.class)
    ResponseEntity<Map<String, String>> onUserNotFound(UserNotFound exception) {
        Map<String, String> body = new HashMap<>();
        body.put("message", exception.getMessage());
        log.info("User not found: {}", body);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemNotFound.class)
    ResponseEntity<Map<String, String>> onItemNotFound(ItemNotFound exception) {
        Map<String, String> body = new HashMap<>();
        body.put("message", exception.getMessage());
        log.info("Item not found: {}", body);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommentNotExists.class)
    ResponseEntity<Map<String, String>> onCommentNotExists(CommentNotExists exception) {
        Map<String, String> body = new HashMap<>();
        body.put("message", exception.getMessage());
        log.info("Commenting not exists: {}", body);
        return new ResponseEntity<>(body, BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Map<String, String>> onConstraintViolation(ConstraintViolationException exception) {
        Map<String, String> body = new HashMap<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            body.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        log.info("BeanValidation fail: {}", body);
        return new ResponseEntity<>(body, BAD_REQUEST);
    }
}