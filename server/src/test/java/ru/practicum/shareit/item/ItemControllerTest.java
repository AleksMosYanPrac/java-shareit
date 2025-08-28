package ru.practicum.shareit.item;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfo;
import ru.practicum.shareit.item.exceptions.CommentNotExists;
import ru.practicum.shareit.item.exceptions.ItemNotFound;
import ru.practicum.shareit.item.interfaces.ItemService;
import ru.practicum.shareit.user.exceptions.UserNotFound;

import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ItemController.class})
public class ItemControllerTest {

    private String path = "/items";
    private String header = "X-Sharer-User-Id";
    private Long value = 1L;
    private MediaType json = MediaType.APPLICATION_JSON;
    private String pathVariableItemId = "/1";
    private Long userId = 1L;
    private Long itemId = 1L;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    private ItemDto itemDto;
    private ItemInfo itemInfo;
    private String newItemJson;
    private String commentJson;

    @BeforeEach
    void setUp() {
        this.itemDto = TestItemData.getItemDto();
        this.newItemJson = TestItemData.getNewItemJson();
        this.commentJson = TestItemData.getNewCommentJson();
    }

    @Test
    void canTakePOSTRequestForAddNewItem() throws Exception {
        when(itemService.addNewItem(anyLong(), any())).thenReturn(itemDto);

        mockMvc.perform(post(path).contentType(json).header(header, value).content(newItemJson))
                .andExpect(status().isOk());
    }

    @Test
    void canTakePATCHRequestForUpdateItem() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any())).thenReturn(itemDto);

        mockMvc.perform(patch(path + pathVariableItemId)
                        .contentType(json)
                        .header(header, value)
                        .content(newItemJson))
                .andExpect(status().isOk());
    }

    @Test
    void canTakeGETRequestForFindItemById() throws Exception {
        when(itemService.getItemById(anyLong())).thenReturn(itemInfo);

        mockMvc.perform(get(path + pathVariableItemId).contentType(json).header(header, value))
                .andExpect(status().isOk());
    }

    @Test
    void canTakeGETRequestForFindUserItems() throws Exception {
        when(itemService.getUserItems(anyLong())).thenReturn(List.of());

        mockMvc.perform(get(path).contentType(json).header(header, value))
                .andExpect(status().isOk());
    }

    @Test
    void canTakeGETRequestForSearchItemByNameContainString() throws Exception {
        when(itemService.getAvailableItemsByNameContains(any())).thenReturn(List.of());

        mockMvc.perform(get(path + "/search").contentType(json).param("text", "any"))
                .andExpect(status().isOk());
    }

    @Test
    void canTakePOSTRequestForAddCommentToItem() throws Exception {
        when(itemService.addUserCommentToItem(anyLong(), anyLong(), any())).thenReturn(new CommentDto());

        mockMvc.perform(post(path + pathVariableItemId + "/comment")
                        .contentType(json)
                        .header(header, value)
                        .content(commentJson))
                .andExpect(status().isOk());
    }

    @Test
    void canHandleUserNotFoundExceptionThanHttpStatusIsForbidden() throws Exception {
        when(itemService.addNewItem(anyLong(), any())).thenThrow(new UserNotFound(userId));

        mockMvc.perform(post(path).contentType(json).header(header, value).content(newItemJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void canHandleItemNotFoundExceptionThanHttpStatusIsNotFound() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any())).thenThrow(new ItemNotFound(itemId));

        mockMvc.perform(patch(path + pathVariableItemId)
                        .contentType(json)
                        .header(header, value)
                        .content(newItemJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void canHandleCommentNotExistsThanHttpStatusIsBadRequest() throws Exception {
        when(itemService.addUserCommentToItem(anyLong(), anyLong(), any()))
                .thenThrow(new CommentNotExists(userId, itemId));

        mockMvc.perform(post(path + pathVariableItemId + "/comment")
                        .contentType(json)
                        .header(header, value)
                        .content(commentJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void canHandleConstraintViolationBadRequest() throws Exception {
        when(itemService.addNewItem(anyLong(), any()))
                .thenThrow(new ConstraintViolationException("constraint", new HashSet<>()));

        mockMvc.perform(post(path).contentType(json).header(header, value).content(newItemJson))
                .andExpect(status().isBadRequest());
    }
}