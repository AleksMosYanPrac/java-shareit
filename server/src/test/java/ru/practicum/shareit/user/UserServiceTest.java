package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exceptions.UserExists;
import ru.practicum.shareit.user.interfaces.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceTest {

    private final EntityManager em;
    private final UserService userService;

    private long userId = 1L;

    @Test
    void shouldAddNewUser() throws Exception {
        UserDto newUserDto = TestUserData.getNewUserDto();

        userService.addNewUser(newUserDto);

        TypedQuery<User> query = em.createQuery("select u from User u where u.name = :user_name", User.class);
        User user = query.setParameter("user_name", newUserDto.getName()).getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(newUserDto.getName()));
        assertThat(user.getEmail(), equalTo(newUserDto.getEmail()));
    }

    @Test
    void shouldThrowUserExistOnAddNewUser() throws Exception {
        UserDto newUserDto = TestUserData.getNewUserDto();
        userService.addNewUser(newUserDto);

        assertThrows(UserExists.class, () -> userService.addNewUser(newUserDto));
    }

    @Test
    @Sql("/db/h2/tests/add_users.sql")
    void shouldUpdateUser() throws Exception {
        UserDto updatedUserDto = TestUserData.getUserDto();
        userService.updateUser(userId, updatedUserDto);

        TypedQuery<User> query = em.createQuery("select u from User u where u.name = :user_name", User.class);
        User user = query.setParameter("user_name", updatedUserDto.getName()).getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getId(), equalTo(userId));
        assertThat(user.getName(), equalTo(updatedUserDto.getName()));
        assertThat(user.getEmail(), equalTo(updatedUserDto.getEmail()));
    }

    @Test
    @Sql("/db/h2/tests/add_users.sql")
    void shouldUpdateOnlyUserName() throws Exception {
        UserDto updatedUserDto = TestUserData.getUserDto();
        String email = updatedUserDto.getEmail();
        updatedUserDto.setEmail(null);
        userService.updateUser(userId, updatedUserDto);

        TypedQuery<User> query = em.createQuery("select u from User u where u.name = :user_name", User.class);
        User user = query.setParameter("user_name", updatedUserDto.getName()).getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getId(), equalTo(userId));
        assertThat(user.getName(), equalTo(updatedUserDto.getName()));
        assertThat(user.getEmail(), equalTo(email));
    }

    @Test
    @Sql("/db/h2/tests/add_users.sql")
    void shouldUpdateOnlyUserEmail() throws Exception {
        UserDto updatedUserDto = TestUserData.getUserDto();
        String name = updatedUserDto.getName();
        updatedUserDto.setName(null);
        userService.updateUser(userId, updatedUserDto);

        TypedQuery<User> query = em.createQuery("select u from User u where u.email = :user_email", User.class);
        User user = query.setParameter("user_email", updatedUserDto.getEmail()).getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getId(), equalTo(userId));
        assertThat(user.getName(), equalTo(name));
        assertThat(user.getEmail(), equalTo(updatedUserDto.getEmail()));
    }

    @Test
    @Sql("/db/h2/tests/add_users.sql")
    void shouldThrowUserExistOnUpdateUser() throws Exception {
        UserDto updatedUserDto = TestUserData.getUserDto();
        updatedUserDto.setEmail("a@a2.test");

        assertThrows(UserExists.class, () -> userService.updateUser(userId, updatedUserDto));
    }

    @Test
    @Sql("/db/h2/tests/add_users.sql")
    void shouldGetUserById() throws Exception {
        UserDto userById = userService.getUserById(userId);

        assertThat(userById.getId(), equalTo(userId));
        assertThat(userById.getName(), notNullValue());
        assertThat(userById.getEmail(), notNullValue());
    }

    @Test
    @Sql("/db/h2/tests/add_users.sql")
    void shouldDeleteUserById() throws Exception {
        userService.deleteUserById(userId);

        TypedQuery<User> query = em.createQuery("select u from User u", User.class);
        List<Long> userIdList = query.getResultList().stream().map(User::getId).toList();

        assertThat(userIdList.isEmpty(), equalTo(false));
        assertThat(userIdList.contains(userId), equalTo(false));
    }
}