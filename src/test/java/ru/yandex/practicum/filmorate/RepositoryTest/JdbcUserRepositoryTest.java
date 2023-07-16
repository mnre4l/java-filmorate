package ru.yandex.practicum.filmorate.RepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.repository.user.dao.DbUsersRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(DbUsersRepository.class)
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JdbcUserRepositoryTest {
    @Autowired
    @Qualifier("DbUsersRepository")
    private UserRepository userRepository;

    @Test
    public void shouldFindTestUserById() {
        User user = userRepository.get(1).get();

        assertNotNull(user);

        assertEquals("test@test.ru", user.getEmail());
        assertEquals("test login", user.getLogin());
        assertEquals("test name", user.getName());
        assertEquals(LocalDate.of(2000, 8, 17), user.getBirthday());
        assertEquals(1, user.getId());
    }

    @Test
    public void shouldNotFindTestUserWithBadId() {
        assertEquals(Optional.empty(), userRepository.get(9999));
    }

    @Test
    public void shouldCreateUser() {
        User user = new User();

        user.setName("test name 2");
        user.setEmail("test2@test2.ru");
        user.setBirthday(LocalDate.of(2000, 2, 12));
        user.setLogin("test login 2");
        userRepository.create(user);
        assertEquals(user, userRepository.get(2).get());
    }

    @Test
    public void shouldUpdateTestUser() {
        User user = userRepository.get(1).get();

        user.setName("test name 2");
        user.setEmail("test2@test2.ru");
        user.setBirthday(LocalDate.of(2000, 2, 12));
        user.setLogin("test login 2");

        assertNotEquals(user, userRepository.get(1).get());
        userRepository.update(user);
        assertEquals(user, userRepository.get(1).get());
    }

    @Test
    public void shouldDeleteTestUser() {
        User user = userRepository.get(1).get();

        userRepository.delete(user);
        assertEquals(Optional.empty(), userRepository.get(1));
    }

    @Test
    public void shouldReturnAllUsers() {
        User user = userRepository.get(1).get();

        assertEquals(List.of(user), userRepository.getAll());

        User user2 = new User();

        user2.setName("test name 2");
        user2.setEmail("test2@test2.ru");
        user2.setBirthday(LocalDate.of(2000, 2, 12));
        user2.setLogin("test login 2");
        userRepository.create(user2);
        assertEquals(List.of(user, user2), userRepository.getAll());
    }

    @Test
    public void shouldDeleteAllUsers() {
        User user2 = new User();

        user2.setName("test name 2");
        user2.setEmail("test2@test2.ru");
        user2.setBirthday(LocalDate.of(2000, 2, 12));
        user2.setLogin("test login 2");
        userRepository.create(user2);

        assertEquals(List.of(userRepository.get(1).get(), user2), userRepository.getAll());
        userRepository.deleteAll();
        assertEquals(new ArrayList<>(), userRepository.getAll());
    }

    @Test
    public void shouldReturnAllUsersIds() {
        User user2 = new User();

        user2.setName("test name 2");
        user2.setEmail("test2@test2.ru");
        user2.setBirthday(LocalDate.of(2000, 2, 12));
        user2.setLogin("test login 2");
        userRepository.create(user2);
        assertEquals(List.of(1, 2), userRepository.getAllIds());
    }

    @Test
    public void shouldAddToFriends() {
        User friend = new User();

        friend.setName("friend name");
        friend.setEmail("friend@friend.ru");
        friend.setBirthday(LocalDate.of(2000, 2, 12));
        friend.setLogin("friend login");
        userRepository.create(friend);
        assertEquals(new ArrayList<>(), userRepository.getFriends(1));
        userRepository.addToFriends(1, friend.getId());
        assertEquals(List.of(friend), userRepository.getFriends(1));
    }

    @Test
    public void shouldDeleteFromFriends() {
        User friend = new User();

        friend.setName("friend name");
        friend.setEmail("friend@friend.ru");
        friend.setBirthday(LocalDate.of(2000, 2, 12));
        friend.setLogin("friend login");
        userRepository.create(friend);
        userRepository.addToFriends(1, friend.getId());
        assertEquals(List.of(friend), userRepository.getFriends(1));
        userRepository.deleteFromFriends(1, friend.getId());
        assertEquals(new ArrayList<>(), userRepository.getFriends(1));
    }

    @Test
    public void shouldReturnConfirmFriends() {
        User friend = new User();

        friend.setName("friend name");
        friend.setEmail("friend@friend.ru");
        friend.setBirthday(LocalDate.of(2000, 2, 12));
        friend.setLogin("friend login");
        userRepository.create(friend);
        userRepository.addToFriends(1, friend.getId());
        // пользователь friend не подтвердил дружбу
        assertEquals(new ArrayList<>(), userRepository.getConfirmFriends(1));
        userRepository.addToFriends(friend.getId(), 1);
        // пользователь friend подтвердил дружбу
        assertEquals(List.of(friend), userRepository.getConfirmFriends(1));
        assertEquals(List.of(userRepository.get(1).get()), userRepository.getConfirmFriends(friend.getId()));
    }
}
