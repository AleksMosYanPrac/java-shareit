package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exceptions.UserExists;
import ru.practicum.shareit.user.exceptions.UserNotFound;
import ru.practicum.shareit.user.interfaces.UserMapper;
import ru.practicum.shareit.user.interfaces.UserRepository;
import ru.practicum.shareit.user.interfaces.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public UserDto addNewUser(UserDto user) throws UserExists {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserExists();
        }
        User newUser = mapper.toUser(user);
        newUser = userRepository.save(newUser);
        return mapper.toUserDto(newUser);
    }

    @Override
    public UserDto updateUser(long userId, UserDto userData) throws UserNotFound, UserExists {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFound(userId));
        if (userData.getName() != null) {
            user = user.toBuilder().name(userData.getName()).build();
        }
        if (userData.getEmail() != null) {
            Optional<User> userByEmail = userRepository.findByEmail(userData.getEmail());
            if (userByEmail.isPresent() && userByEmail.get().getId() != userId) {
                throw new UserExists();
            }
            user = user.toBuilder().email(userData.getEmail()).build();
        }
        userRepository.save(user);
        return mapper.toUserDto(user);
    }

    @Override
    public UserDto getUserById(long userId) throws UserNotFound {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFound(userId));
        return mapper.toUserDto(user);
    }

    @Override
    public void deleteUserById(long userId) throws UserNotFound {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFound(userId));
        userRepository.delete(user);
    }
}