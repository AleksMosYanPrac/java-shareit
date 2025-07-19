package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfo;
import ru.practicum.shareit.request.interfaces.ItemRequestService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceTest {

    private final EntityManager em;
    private final ItemRequestService requestService;

    private long userId = 1L;
    private long requestId = 1L;

    @Test
    @Sql("/db/h2/tests/add_users.sql")
    void addItemRequest() throws Exception {
        ItemRequestDto itemRequestDto = TestRequestData.getNewItemRequest();

        requestService.addItemRequest(userId, itemRequestDto);

        TypedQuery<ItemRequest> query = em.createQuery(
                "select ir from ItemRequest ir where ir.description = :description", ItemRequest.class);
        ItemRequest itemRequest = query.setParameter(
                "description", itemRequestDto.getDescription()).getSingleResult();

        assertThat(itemRequest, notNullValue());
        assertThat(itemRequest.getDescription(), equalTo(itemRequestDto.getDescription()));
    }

    @Test
    @Sql("/db/h2/tests/add_users.sql")
    @Sql("/db/h2/tests/add_item_requests.sql")
    void getUsersRequests() throws Exception {
        List<ItemRequestDto> requests = requestService.getUsersRequests(userId);

        assertThat(requests.isEmpty(), equalTo(false));
        assertThat(requests.size(), equalTo(2));
    }

    @Test
    @Sql("/db/h2/tests/add_users.sql")
    @Sql("/db/h2/tests/add_item_requests.sql")
    void getRequestInfoById() throws Exception {
        ItemRequestInfo requestInfo = requestService.getRequestInfoById(userId, requestId);

        assertThat(requestInfo, notNullValue());
        assertThat(requestInfo.getId(), equalTo(requestId));
    }

    @Test
    @Sql("/db/h2/tests/add_users.sql")
    @Sql("/db/h2/tests/add_item_requests.sql")
    void getAllRequests() {
        List<ItemRequestDto> requests = requestService.getAllRequests();

        assertThat(requests.isEmpty(), equalTo(false));
        assertThat(requests.size(), equalTo(3));
    }
}