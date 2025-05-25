package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.interfaces.ItemRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> itemCollection;

    @Override
    public Item save(Item item) {
        if (Objects.isNull(item.getId())) {
            item.setId(calculateId());
        }
        return itemCollection.compute(item.getId(), (k, v) -> item);
    }

    @Override
    public Optional<Item> getItemById(long itemId) {
        return itemCollection.values().stream().filter(i -> i.getId().equals(itemId)).findFirst();
    }

    @Override
    public List<Item> findAllOwnerItems(long ownerId) {
        return itemCollection.values().stream().filter(i -> i.getOwnerId().equals(ownerId)).toList();
    }

    @Override
    public List<Item> findAllAvailableItemsByNameContains(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return itemCollection.values()
                .stream()
                .filter(Item::getAvailable)
                .filter(i -> i.getName().toLowerCase().contains(text.toLowerCase()))
                .toList();
    }

    private Long calculateId() {
        return itemCollection.keySet().stream().mapToLong(Long::longValue).max().orElse(0) + 1;
    }
}