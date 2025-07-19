package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfo;

import java.time.LocalDateTime;
import java.util.Set;

public class TestRequestData {

    public static ItemRequestDto getRequestDto() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("test description");
        itemRequestDto.setCreated(LocalDateTime.now());
        return itemRequestDto;
    }

    public static ItemRequestInfo getRequestInfo() {
        ItemRequestInfo itemRequestInfo = new ItemRequestInfo();
        itemRequestInfo.setId(1L);
        itemRequestInfo.setItems(Set.of());
        itemRequestInfo.setDescription("test description");
        itemRequestInfo.setCreated(LocalDateTime.now());
        return itemRequestInfo;
    }

    public static String getItemRequestJson() {
        return "{\n" +
               "  \"description\": \"test item request\"\n" +
               "}\n";
    }

    public static ItemRequestDto getNewItemRequest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("test description");
        return itemRequestDto;
    }
}