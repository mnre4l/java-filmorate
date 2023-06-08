package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.repository.FilmsRepository;
import ru.yandex.practicum.filmorate.service.Marker;
import ru.yandex.practicum.filmorate.service.ValidationException;
import javax.validation.Valid;
import java.util.List;


/**
 * Класс-контролер, обслуживающий фильмы.
 */
@RestController
@Slf4j
@Validated
public class FilmController {

    /**
     * Репозиторий фильмов.
     */
    @Autowired
    private FilmsRepository filmsRepository;
    /**
     * Эндпоинт GET /films.
     * @return список фильмов.
     */
    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("GET /films");
        return filmsRepository.getAll();
    }

    /**
     * Эндпоинт POST /films.
     * @param film тело запроса
     * @return созданный объект фильма в случае успеха.
     */
    @PostMapping("/films")
    @Validated({Marker.OnCreate.class})
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("POST /films: получен для " + film);
        filmsRepository.create(film);
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
        log.info("PUT /films: получен для " + film.getId());
        filmsRepository.update(film);
        log.info("PUT /films: " + film.getId());
        return film;
    }

    /**
     * Обработка исключения валидации валидации.
     * @param e исключение типа MethodArgumentNotValidException, бросаемое валидатором
     * @return пришедший объект (по условию прохождения тестов). При создании ValidationException
     * логируется ошибка
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Film> handleValidationException(MethodArgumentNotValidException e, @RequestBody Film film) {
        new ValidationException("Ошибка валидации: " + e.getBindingResult().getAllErrors());
        return new ResponseEntity<>(film, HttpStatus.BAD_REQUEST);
    }

}
