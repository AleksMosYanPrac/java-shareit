package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfo;
import ru.practicum.shareit.item.exceptions.CommentNotExists;
import ru.practicum.shareit.item.exceptions.ItemNotFound;
import ru.practicum.shareit.item.interfaces.ItemService;
import ru.practicum.shareit.request.exceptions.RequestNotFound;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {

    private final EntityManager em;
    private final ItemService itemService;

    private long userId = 1L;
    private long itemId = 1L;

    @Test
    @Sql("/db/h2/tests/add_users.sql")
    void shouldAddNewItem() throws Exception {
        ItemDto newItem = TestItemData.getNewItemDto();
        itemService.addNewItem(userId, newItem);

        TypedQuery<Item> query = em.createQuery("select i from Item i where i.name = :item_name", Item.class);
        Item item = query.setParameter("item_name", newItem.getName()).getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getOwnerId(), equalTo(userId));
        assertThat(item.getName(), equalTo(newItem.getName()));
        assertThat(item.getAvailable(), equalTo(newItem.getAvailable()));
    }

    @Test
    @Sql("/db/h2/tests/add_users.sql")
    void shouldThrowRequestNotFoundOnAddNewItemWhenRequestIsAbsent() {
        ItemDto newItem = TestItemData.getNewItemDto();
        newItem.setRequestId(1L);

        assertThrows(RequestNotFound.class, () -> itemService.addNewItem(userId, newItem));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    void shouldUpdateItem() throws Exception {
        ItemDto updatedItem = TestItemData.getItemDto();
        updatedItem.setAvailable(false);
        itemService.updateItem(userId, itemId, updatedItem);

        TypedQuery<Item> query = em.createQuery("select i from Item i where i.name = :item_name", Item.class);
        Item item = query.setParameter("item_name", updatedItem.getName()).getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getOwnerId(), equalTo(userId));
        assertThat(item.getName(), equalTo(updatedItem.getName()));
        assertThat(item.getAvailable(), equalTo(updatedItem.getAvailable()));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    void shouldUpdateOnlyItemName() throws Exception {
        ItemDto updatedItem = TestItemData.getItemDto();
        String description = updatedItem.getDescription();
        updatedItem.setDescription(null);
        itemService.updateItem(userId, itemId, updatedItem);

        TypedQuery<Item> query = em.createQuery("select i from Item i where i.name = :item_name", Item.class);
        Item item = query.setParameter("item_name", updatedItem.getName()).getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getOwnerId(), equalTo(userId));
        assertThat(item.getName(), equalTo(updatedItem.getName()));
        assertThat(item.getDescription(), equalTo(description));
        assertThat(item.getAvailable(), equalTo(updatedItem.getAvailable()));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    void shouldUpdateOnlyItemDescription() throws Exception {
        ItemDto updatedItem = TestItemData.getItemDto();
        String name = updatedItem.getName();
        updatedItem.setName(null);
        itemService.updateItem(userId, itemId, updatedItem);

        TypedQuery<Item> query = em.createQuery("select i from Item i where i.description = :description", Item.class);
        Item item = query.setParameter("description", updatedItem.getDescription()).getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getOwnerId(), equalTo(userId));
        assertThat(item.getName(), equalTo(name));
        assertThat(item.getDescription(), equalTo(updatedItem.getDescription()));
        assertThat(item.getAvailable(), equalTo(updatedItem.getAvailable()));
    }


    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    void shouldThrowItemNotFoundOnUpdateItemWhenItemNotBelongsToOwner() {
        ItemDto updatedItem = TestItemData.getItemDto();
        updatedItem.setId(2L);

        assertThrows(ItemNotFound.class, () -> itemService.updateItem(userId, updatedItem.getId(), updatedItem));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    void shouldGetItemById() throws Exception {
        ItemInfo item = itemService.getItemById(itemId);

        assertThat(item.getId(), equalTo(itemId));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    void shouldGetUserItems() throws Exception {
        List<ItemDto> items = itemService.getUserItems(userId);

        assertThat(items.size(), equalTo(1));
    }

    @Test
    @Sql("/db/h2/tests/add_users_and_item.sql")
    void shouldGetAvailableItemsByNameContains() {
        List<ItemDto> items = itemService.getAvailableItemsByNameContains("it");

        assertThat(items.size(), equalTo(2));
    }

    @Test
    void shouldGetAvailableItemsByNameContainsWhenTextIsNull() {
        List<ItemDto> items = itemService.getAvailableItemsByNameContains(null);

        assertThat(items.size(), equalTo(0));
    }

    @Test
    void shouldGetAvailableItemsByNameContainsWhenTextIsBlank() {
        List<ItemDto> items = itemService.getAvailableItemsByNameContains("");

        assertThat(items.size(), equalTo(0));
    }

    @Test
    @Sql("/db/h2/tests/add_users.sql")
    @Sql("/db/h2/tests/add_item_and_booking.sql")
    void shouldAddUserCommentToItem() throws Exception {
        long itemId = 2L;
        CommentDto dto = itemService.addUserCommentToItem(userId, itemId, TestItemData.getNewCommentDto());

        TypedQuery<Comment> query = em.createQuery("select c from Comment c where c.id = :comment_id", Comment.class);
        Comment comment = query.setParameter("comment_id", dto.getId()).getSingleResult();

        assertThat(comment.getId(), notNullValue());
        assertThat(comment.getText(), equalTo(dto.getText()));
    }

    @Test
    @Sql("/db/h2/tests/add_users.sql")
    @Sql("/db/h2/tests/add_item_and_booking.sql")
    void shouldThrowCommentNotExistOnAddUserCommentToItemWhenUserNotBookedItem() {
        long itemId = 1L;

        assertThrows(CommentNotExists.class,
                () -> itemService.addUserCommentToItem(userId, itemId, TestItemData.getNewCommentDto()));
    }
}