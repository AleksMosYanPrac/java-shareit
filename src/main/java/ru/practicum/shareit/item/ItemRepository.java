package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Validated
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> getItemById(long itemId);

    List<Item> findAllByOwnerId(long ownerId);

    List<Item> findByAvailableTrueAndNameContainingIgnoreCase(@NotNull String text);
}