package ru.practicum.shareit.user;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exceptions.UserExists;
import ru.practicum.shareit.user.exceptions.UserNotFound;
import ru.practicum.shareit.user.interfaces.UserService;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UserController.class})
class UserControllerTest {

    private String path = "/users";
    private MediaType json = MediaType.APPLICATION_JSON;
    private String pathVariableUserId = "/1";

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private UserDto userDto;
    private String userDataJson;

    @BeforeEach
    void setUp() {
        this.userDto = TestUserData.getUserDto();
        this.userDataJson = TestUserData.getUserDataJson();
    }

    @Test
    void canTakePOSTRequestForAddNewUser() throws Exception {
        when(userService.addNewUser(any())).thenReturn(userDto);

        mockMvc.perform(post(path).contentType(json).content(userDataJson))
                .andExpect(status().isOk());
    }

    @Test
    void canTakePATCHRequestForUpdateUserData() throws Exception {
        when(userService.updateUser(anyLong(), any())).thenReturn(userDto);

        mockMvc.perform(patch(path + pathVariableUserId).contentType(json).content(userDataJson))
                .andExpect(status().isOk());
    }

    @Test
    void canTakeGETRequestForFindUserById() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(userDto);

        mockMvc.perform(get(path + pathVariableUserId).contentType(json))
                .andExpect(status().isOk());
    }

    @Test
    void canTakeDELETERequestForDeleteUser() throws Exception {
        doNothing().when(userService).deleteUserById(anyLong());

        mockMvc.perform(delete(path + pathVariableUserId).contentType(json))
                .andExpect(status().isOk());
    }

    @Test
    void canHandleUserExistsExceptionThanHttpStatusIsConflict() throws Exception {
        when(userService.addNewUser(any()))
                .thenThrow(new UserExists());

        mockMvc.perform(post(path).contentType(json).content(userDataJson))
                .andExpect(status().isConflict());
    }

    @Test
    void canHandleUserNotFoundExceptionThanHttpStatusIsNotFound() throws Exception {
        when(userService.updateUser(anyLong(), any()))
                .thenThrow(new UserNotFound(1L));

        mockMvc.perform(patch(path + pathVariableUserId).contentType(json).content(userDataJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void canHandleConstraintViolationBadRequest() throws Exception {
        when(userService.addNewUser(any()))
                .thenThrow(new ConstraintViolationException("constraint", new HashSet<>()));

        mockMvc.perform(post(path).contentType(json).content(userDataJson))
                .andExpect(status().isBadRequest());
    }
}