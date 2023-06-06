package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.Marker;
import ru.yandex.practicum.filmorate.service.ValidationException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;


/**
 * Класс-контролер, обслуживающий фильмы.
 */
@RestController
@Slf4j
@Validated
public class FilmController {
    /**
     * Хранимые фильмы.
     */
    private HashMap<Integer, Film> films = new HashMap<>();
    private int id;
    /**
     * Эндпоинт GET /films.
     * @return список фильмов.
     */
    @GetMapping("/films")
    public List<Film> getFilms() {
        return List.copyOf(films.values());
    }

    /**
     * Эндпоинт POST /films.
     * @param film тело запроса
     * @return созданный объект фильма в случае успеха.
     */
    @PostMapping("/films")
    @Validated({Marker.OnCreate.class})
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        log.info("POST /films: " + film);
        return film;
    }

    /**
     * Эндпоинт PUT /films
     * @param film тело запроса
     * @return обновленный объект фильма в случае успеха
     */
    @PutMapping("/films")
    @Validated({Marker.OnUpdate.class})
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("PUT /films: " + film);
            return film;
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
    public ResponseEntity<Film> handleValidationException(MethodArgumentNotValidException e, @RequestBody Film film) {
        ValidationException ex = new ValidationException("Ошибка валидации: " + e.getBindingResult().getAllErrors());
        return new ResponseEntity<>(film, HttpStatus.BAD_REQUEST);
    }

}
