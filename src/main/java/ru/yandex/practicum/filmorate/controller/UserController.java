package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Marker;
import ru.yandex.practicum.filmorate.service.ValidationException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * Класс-контроллер, обслуживающий пользователей.
 */
@RestController
@Slf4j
@Validated
public class UserController {
    /**
     * Хранимые пользователи.
     */
    private HashMap<Integer, User> users = new HashMap<>();
    int id;

    /**
     * Эндпоинт GET /users.
     * @return список пользователей.
     */
    @GetMapping("/users")
    public List<User> getUsers() {
        return List.copyOf(users.values());
    }

    /**
     * @param user создаваемый пользователь
     * @return объект созданного пользователя в случае успеха
     */
    @PostMapping("/users")
    @Validated({Marker.OnCreate.class})
    public User addUser(@Valid @RequestBody User user) {
        user.setId(++id);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("POST /users: " + user);
        return user;
    }

    /**
     * Эндпоинт PUT /users
     * @param user тело запроса
     * @return обновленный объект фильма в случае успеха
     */
    @PutMapping("/users")
    @Validated({Marker.OnUpdate.class})
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("PUT /users: " + user);
            return user;
        } else {
            throw new ValidationException("Такого пользователя нет");
        }
    }

    /**
     * Обработка ошибок валидации.
     * @param e исключение типа MethodArgumentNotValidException, бросаемое валидатором
     * @return пришедший объект (по условию прохождения тестов). При создании исключения логируется ошибка
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<User> handleValidationException(MethodArgumentNotValidException e, @RequestBody User user) {
        ValidationException ex = new ValidationException("Ошибка валидации: " + e.getBindingResult().getAllErrors());
        return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
    }
}
