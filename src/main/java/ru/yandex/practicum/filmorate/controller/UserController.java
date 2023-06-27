package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.validation.Marker;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Set;


/**
 * Класс-контроллер, обслуживающий пользователей.
 */
@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Эндпоинт GET /users.
     *
     * @return список пользователей.
     */
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getUsers() {
        log.info("GET /users/");
        return userService.getAll();
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUsers(@PathVariable("id") int id) {
        log.info("GET /users/" + id);
        return userService.getUser(id);
    }

    /**
     * Эндпоинт POST /users
     *
     * @param user создаваемый пользователь
     * @return объект созданного пользователя в случае успеха
     */
    @PostMapping("/users")
    @Validated({Marker.OnCreate.class})
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Valid @RequestBody User user) {
        log.info("POST /users: получен для " + user);
        userService.create(user);
        log.info("POST /users: " + user);
        return user;
    }

    /**
     * Эндпоинт PUT /users
     *
     * @param user тело запроса
     * @return обновленный объект фильма в случае успеха
     */
    @PutMapping("/users")
    @Validated({Marker.OnUpdate.class})
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody User user) {
        log.info("PUT /users: получен для " + user.getId());
        userService.update(user);
        log.info("PUT /users: " + user.getId());
        return user;
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        log.info("PUT /users/{id}/friends/{friendId}: получен для id = " + id + ", friendId = " + friendId);
        userService.addFriend(id, friendId);
        log.info("Пользователь с id = " + id + " и пользователь с id = " + friendId + " добавлены в друзья.");
        return userService.getUser(friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User deleteFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        log.info("DELETE /users/{id}/friends/{friendId}: получен для id = " + id + ", friendId = " + friendId);
        userService.deleteFriend(id, friendId);
        log.info("Пользователь с id = " + id + " и пользователь с id = " + friendId + " удалены из друзей.");
        return userService.getUser(friendId);
    }

    @GetMapping("/users/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Set<User> getFriends(@PathVariable("id") int id) {
        log.info("GET /users/{id}/friends: получен для id = " + id);
        Set<User> friends = userService.getFriends(id);
        log.info("Возвращен список друзей: " + friends);
        return friends;
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Set<User> getCommonFriends(@PathVariable("id") int id,
                                      @PathVariable(value = "otherId", required = false) int otherId) {
        log.info("GET /users/{id}/friends/common/{otherId}: получен для id = " + id + ", otherId = " + otherId);
        Set<User> friends = userService.getCommonFriends(id, otherId);
        log.info("Возвращен список общих друзей: " + friends);
        return friends;
    }
}
