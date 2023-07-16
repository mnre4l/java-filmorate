package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.validation.Marker;

import javax.validation.Valid;
import java.util.List;


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
    public List<User> getUsers() {
        log.info("GET /users/");
        return userService.getAll();
    }

    /**
     * Эндпоинт GET /users/{id}
     *
     * @param id id заправшиваемого пользователя.
     * @return объект запрашиваемого пользователя.
     */
    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable("id") int id) {
        log.info("GET /users/" + id);
        return userService.getUser(id);
    }

    /**
     * Эндпоинт POST /users.
     * Предназнчен для создания пользователя.
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
     * Предназначен для обновления данных пользователя.
     *
     * @param user тело запроса (json соответствующий модели пользователя)
     * @return обновленный объект фильма в случае успеха
     */
    @PutMapping("/users")
    @Validated({Marker.OnUpdate.class})
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody User user) {
        log.info("PUT /users: получен для id = " + user.getId());
        userService.update(user);
        log.info("PUT /users: обновлен пользователь id = " + user.getId());
        return user;
    }

    /**
     * Эндпоинт PUT /users/{id}/friends/{friendId}.
     * Предназначен для добавления одного пользователя в друзья другого.
     *
     * @param id       пользователь, от которого запрос на добавление в друзья.
     * @param friendId пользователь, которому запрос на добавление в друзья.
     * @return добавленный в друзья пользователь.
     */
    @PutMapping("/users/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        log.info("PUT /users/{id}/friends/{friendId}: получен для id = " + id + ", friendId = " + friendId);
        userService.addFriend(id, friendId);
        log.info("Пользователь с id = " + id + " добавлен в друзья к пользователю с id = " + friendId);
        return userService.getUser(friendId);
    }

    /**
     * Эндпоинт DEL /users/{id}/friends/{friendId}.
     * Предназначен для удаления пользователя из друзей.
     *
     * @param id       пользователь, который удаляет из друзей другого пользователя.
     * @param friendId удаляемый из друзей пользователь.
     * @return удаленный из друзей пользователь.
     */
    @DeleteMapping("/users/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User deleteFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        log.info("DELETE /users/{id}/friends/{friendId}: получен для id = " + id + ", friendId = " + friendId);
        userService.deleteFriend(id, friendId);
        log.info("Пользователь с id = " + id + " удалил пользователя с id = " + friendId + " из друзей.");
        return userService.getUser(friendId);
    }

    /**
     * Эндпоинт GET /users/{id}/friends.
     * Предназначен для получения списка друзей пользователей.
     * Друзьями пользователя считаются все пользователи, которым он отправил запрос на дружбу.
     *
     * @param id      пользователь, список друзей которого вернется.
     * @param confirm указывает на то, нужно ли вернуть подтвержденных друзей. Подтвержденными друзьями считаются те,
     *                кто отправлял запрос на друзья в ответ
     * @return список друзей пользователя.
     */
    @GetMapping("/users/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getFriends(@PathVariable("id") int id,
                                 @RequestParam(required = false, defaultValue = "false") boolean confirm) {
        log.info("GET /users/{id}/friends: получен для id = " + id + ". Параметр confirm = " + confirm);
        List<User> friends = userService.getFriends(id, confirm);
        log.info("Возвращен список друзей: " + friends);
        return friends;
    }

    /**
     * Эндпоинт GET /users/{id}/friends/common/{otherId}.
     * Предназначен для получения списка общих друзей двух пользователей.
     *
     * @param id      первый пользователь.
     * @param otherId второй пользователь.
     * @return список общих друзей.
     */
    @GetMapping("/users/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getCommonFriends(@PathVariable("id") int id,
                                       @PathVariable(value = "otherId", required = false) int otherId) {
        log.info("GET /users/{id}/friends/common/{otherId}: получен для id = " + id + ", otherId = " + otherId);
        List<User> friends = userService.getCommonFriends(id, otherId);
        log.info("Возвращен список общих друзей: " + friends);
        return friends;
    }

    /**
     * Эндпоинт DEL /users.
     * Предназначен для удаления всех пользователей из репозитория.
     */
    @DeleteMapping("/users")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteAllUsers() {
        log.info("DELETE /users");
        userService.deleteAllUsers();
        log.info("Все пользователи удалены");
    }
}
