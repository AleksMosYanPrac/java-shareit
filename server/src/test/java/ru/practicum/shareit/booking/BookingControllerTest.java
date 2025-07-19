package ru.practicum.shareit.booking;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exceptions.BookingNotFound;
import ru.practicum.shareit.booking.exceptions.ItemNotAvailable;
import ru.practicum.shareit.booking.interfaces.BookingService;
import ru.practicum.shareit.item.exceptions.ItemNotFound;
import ru.practicum.shareit.user.exceptions.UserNotFound;

import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({BookingController.class})
public class BookingControllerTest {

    private String path = "/bookings";
    private String header = "X-Sharer-User-Id";
    private Long value = 1L;
    private MediaType json = MediaType.APPLICATION_JSON;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    private BookingDto bookingDto;
    private String newBookingJson;

    @BeforeEach
    void setUp() {
        this.bookingDto = TestBookingData.getBookingDto();
        this.newBookingJson = TestBookingData.getNewBookingJson();
    }

    @Test
    void canTakePOSTRequestForAddBooking() throws Exception {
        when(bookingService.addBooking(anyLong(), any())).thenReturn(bookingDto);

        mockMvc.perform(post(path).contentType(json).header(header, value).content(newBookingJson))
                .andExpect(status().isOk());
    }

    @Test
    void canTakePATCHRequestForUpdateBookingStatus() throws Exception {
        when(bookingService.changeBookingStatus(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingDto);

        mockMvc.perform(patch(path + "/1")
                        .contentType(json)
                        .header(header, value)
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void canTakeGETRequestForGetBookingById() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(bookingDto);

        mockMvc.perform(get(path + "/1").contentType(json).header(header, value))
                .andExpect(status().isOk());
    }

    @Test
    void canTakeGETRequestForGetBookersBookingsByState() throws Exception {
        when(bookingService.getBookingsForBooker(anyLong(), any())).thenReturn(List.of());

        mockMvc.perform(get(path).contentType(json).header(header, value))
                .andExpect(status().isOk());
    }

    @Test
    void canTakeGETRequestForGetOwnersBookingsByState() throws Exception {
        when(bookingService.getBookingsForOwner(anyLong(), any())).thenReturn(List.of());

        mockMvc.perform(get(path + "/owner").contentType(json).header(header, value))
                .andExpect(status().isOk());
    }

    @Test
    void canHandleUserNotFoundExceptionThanHttpStatusIsForbidden() throws Exception {
        when(bookingService.addBooking(anyLong(), any())).thenThrow(new UserNotFound(1L));

        mockMvc.perform(post(path).contentType(json).header(header, value).content(newBookingJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void canHandleItemNotFoundExceptionThanHttpStatusIsNotFound() throws Exception {
        when(bookingService.addBooking(anyLong(), any())).thenThrow(new ItemNotFound(1L));

        mockMvc.perform(post(path).contentType(json).header(header, value).content(newBookingJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void canHandleBookingNotFoundExceptionThanHttpStatusIsNotFound() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong())).thenThrow(new BookingNotFound(1L, 1L));

        mockMvc.perform(get(path + "/1").contentType(json).header(header, value))
                .andExpect(status().isNotFound());
    }

    @Test
    void canHandleItemNotAvailableExceptionThanHttpStatusIsBadRequest() throws Exception {
        when(bookingService.addBooking(anyLong(), any())).thenThrow(new ItemNotAvailable(1L));

        mockMvc.perform(post(path).contentType(json).header(header, value).content(newBookingJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void canHandleConstraintViolationBadRequest() throws Exception {
        when(bookingService.addBooking(anyLong(), any()))
                .thenThrow(new ConstraintViolationException("constraint", new HashSet<>()));

        mockMvc.perform(post(path).contentType(json).header(header, value).content(newBookingJson))
                .andExpect(status().isBadRequest());
    }
}