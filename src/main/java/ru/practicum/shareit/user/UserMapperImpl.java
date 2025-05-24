package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.interfaces.UserMapper;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserDto user) {
        return User.builder().id(user.getId()).email(user.getEmail()).name(user.getName()).build();
    }

    @Override
    public UserDto toUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        return dto;
    }
}
