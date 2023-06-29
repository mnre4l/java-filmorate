package ru.yandex.practicum.filmorate.UserServiceTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.repository.user.InMemoryUsersRepository;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class UserServiceWithInMemoryRepositoryTest {
    private UserService userService;
    private ValidateService validateService;
    private UserRepository userRepository;
    private User user = new User();
    private User friend = new User();

    @BeforeEach
    public void init() {
        userRepository = new InMemoryUsersRepository();
        validateService = new ValidateService(null, userRepository);
        userService = new UserService(userRepository, validateService);

        user.setName("User name");
        user.setEmail("email@mail.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(1996, 8, 17));

        friend.setName("Friend name");
        friend.setEmail("friend@mail.ru");
        friend.setLogin("friendlogin");
        friend.setBirthday(LocalDate.of(1995, 8, 17));

        userService.create(user);
        userService.create(friend);
    }

    @AfterEach
    public void clearRepository() {
        userService.deleteAllUsers();
    }

    @Test
    public void shouldCreateUsers() {
        assertTrue(userRepository.getAll().contains(user));
        assertTrue(userRepository.getAll().contains(friend));
    }

    @Test
    public void shouldAddFriendToUser() {
        userService.addFriend(user.getId(), friend.getId());
        assertTrue(userRepository.getFriends(user.getId()).contains(friend));
        assertTrue(userRepository.getFriends(friend.getId()).contains(user));
    }

    @Test
    void shouldDeleteFriendFromUserFriends() {
        shouldAddFriendToUser();
        userService.deleteFriend(user.getId(), friend.getId());
        assertFalse(userRepository.getFriends(user.getId()).contains(friend));
        assertFalse(userRepository.getFriends(friend.getId()).contains(user));
    }

    @Test
    void shouldReturnUserFriends() {
        shouldAddFriendToUser();
        assertEquals(List.of(friend), userService.getFriends(user.getId()));
    }

    @Test
    void shouldReturnCommonFriends() {
        User commonFriend = new User();

        commonFriend.setName("commonFriend name");
        commonFriend.setEmail("commonFriend@mail.ru");
        commonFriend.setLogin("commonFriend");
        commonFriend.setBirthday(LocalDate.of(1996, 8, 17));

        User someFunnyMan = new User();

        someFunnyMan.setName("someFunnyMan name");
        someFunnyMan.setEmail("someFunnyMan@mail.ru");
        someFunnyMan.setLogin("someFunnyMan");
        someFunnyMan.setBirthday(LocalDate.of(1996, 8, 17));

        userService.create(commonFriend);
        userService.create(someFunnyMan);

        userService.addFriend(user.getId(), friend.getId());
        userService.addFriend(user.getId(), commonFriend.getId());
        userService.addFriend(user.getId(), someFunnyMan.getId());

        userService.addFriend(friend.getId(), commonFriend.getId());

        assertEquals(userService.getCommonFriends(user.getId(), friend.getId()), List.of(commonFriend));
    }
}
