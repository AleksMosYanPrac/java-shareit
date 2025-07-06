package ru.practicum.shareit.item.interfaces;

import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public interface CommentMapper {

    Comment toComment(User author, Item item, LocalDateTime created, CommentDto commentDto);

    CommentDto toCommentDto(Comment comment);
}