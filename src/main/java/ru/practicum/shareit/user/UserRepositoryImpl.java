package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.interfaces.UserRepository;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> userCollection;

    @Override
    public User save(User user) {
        if (Objects.isNull(user.getId())) {
            return insertNewUser(user);
        } else {
            return updateUser(user);
        }
    }

    @Override
    public Optional<User> findById(long userId) {
        return userCollection.values().stream().filter(u -> u.getId() == userId).findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userCollection.values().stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }

    @Override
    public void delete(User user) {
        userCollection.remove(user.getId());
    }

    private User updateUser(User user) {
        checkEmail(user);
        userCollection.replace(user.getId(), user);
        return user;
    }

    private User insertNewUser(User user) {
        checkEmail(user);
        User newUser = user.toBuilder().id(calculateId()).build();
        userCollection.put(newUser.getId(), newUser);
        return newUser;
    }

    private void checkEmail(User user) {
        if (userCollection.values()
                .stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()) && !u.getId().equals(user.getId()))) {
            throw new IllegalArgumentException("Email address is already busy");
        }
    }

    private long calculateId() {
        return userCollection.keySet().stream().mapToLong(Long::longValue).max().orElse(0) + 1;
    }
}