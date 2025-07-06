package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.dto.CommentShort;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<CommentShort> findAllByItemId(long itemId);
}