package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UsersRepository;
import ru.yandex.practicum.filmorate.service.Marker;
import ru.yandex.practicum.filmorate.service.ValidationException;
import javax.validation.Valid;
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
    private final UsersRepository usersRepository = new UsersRepository();

    /**
     * Эндпоинт GET /users.
     * @return список пользователей.
     */
    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("GET /users");
        return usersRepository.getAll();
    }

    /**
     * @param user создаваемый пользователь
     * @return объект созданного пользователя в случае успеха
     */
    @PostMapping("/users")
    @Validated({Marker.OnCreate.class})
    public User addUser(@Valid @RequestBody User user) {
        log.info("POST /users: получен для " + user);
        usersRepository.create(user);
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
        log.info("PUT /users: получен для " + user);
        usersRepository.update(user);
        log.info("PUT /users: " + user);
        return user;
    }

    /**
     * Обработка ошибок валидации.
     * @param e исключение типа MethodArgumentNotValidException, бросаемое валидатором
     * @return пришедший объект (по условию прохождения тестов). При создании исключения логируется ошибка
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<User> handleValidationException(MethodArgumentNotValidException e, @RequestBody User user) {
        new ValidationException("Ошибка валидации: " + e.getBindingResult().getAllErrors());
        return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
    }
}
