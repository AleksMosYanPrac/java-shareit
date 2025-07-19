package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public class TestUserData {

    public static UserDto getUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("test");
        userDto.setEmail("test@email.com");
        return userDto;
    }

    public static String getUserDataJson() {
        return "{\n" +
               "  \"name\": \"test\",\n" +
               "  \"email\": \"test@email.com\"\n" +
               "}\n";
    }

    public static UserDto getNewUserDto() {
        UserDto userDto = new UserDto();
        userDto.setName("test");
        userDto.setEmail("test@email.com");
        return userDto;
    }
}