package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfo;
import ru.practicum.shareit.request.interfaces.ItemRequestService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ItemRequestController.class})
class ItemRequestControllerTest {

    private String path = "/requests";
    private String header = "X-Sharer-User-Id";
    private Long value = 1L;
    private MediaType json = MediaType.APPLICATION_JSON;
    private String pathVariableRequestId = "/1";

    @MockBean
    private ItemRequestService requestService;

    @Autowired
    private MockMvc mockMvc;

    private ItemRequestDto itemRequestDto;
    private ItemRequestInfo itemRequestInfo;
    private String newItemRequestJson;

    @BeforeEach
    void setUp() {
        this.itemRequestDto = TestRequestData.getRequestDto();
        this.itemRequestInfo = TestRequestData.getRequestInfo();
        this.newItemRequestJson = TestRequestData.getItemRequestJson();
    }

    @Test
    void canTakePOSTRequestForAddNewItemRequest() throws Exception {
        when(requestService.addItemRequest(anyLong(), any())).thenReturn(itemRequestDto);

        mockMvc.perform(post(path).contentType(json).header(header, value).content(newItemRequestJson))
                .andExpect(status().isOk());
    }

    @Test
    void canTakeGETRequestForFindUserOwnRequests() throws Exception {
        when(requestService.getUsersRequests(anyLong())).thenReturn(List.of());

        mockMvc.perform(get(path).contentType(json).header(header, value))
                .andExpect(status().isOk());
    }

    @Test
    void canTakeGETRequestForFindRequestById() throws Exception {
        when(requestService.getRequestInfoById(anyLong(),anyLong())).thenReturn(itemRequestInfo);

        mockMvc.perform(get(path + pathVariableRequestId).contentType(json).header(header, value))
                .andExpect(status().isOk());
    }

    @Test
    void canTakeGETRequestForFindAllRequests() throws Exception {
        when(requestService.getAllRequests()).thenReturn(List.of());

        mockMvc.perform(get(path + "/all").contentType(json))
                .andExpect(status().isOk());
    }
}