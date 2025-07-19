package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

public class TestItemData {

    public static ItemDto getItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("test");
        itemDto.setAvailable(true);
        itemDto.setDescription("test item");
        return itemDto;
    }

    public static ItemDto getNewItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test");
        itemDto.setAvailable(true);
        itemDto.setDescription("test item");
        return itemDto;
    }

    public static String getNewItemJson() {
        return "{\n" +
               "  \"name\": \"test\",\n" +
               "  \"description\": \"test item\",\n" +
               "  \"available\": true\n" +
               "}\n";
    }

    public static String getNewCommentJson() {
        return "{\n" +
               "  \"text\": \"comment test\"\n" +
               "}\n";
    }

    public static CommentDto getNewCommentDto() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("comment text");
        return commentDto;
    }
}