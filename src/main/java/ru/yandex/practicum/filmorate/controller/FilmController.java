package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.validation.Marker;

import javax.validation.Valid;
import java.util.Collection;


/**
 * Класс-контролер, обслуживающий фильмы.
 */
@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class FilmController {

    /**
     * Сервис фильмов.
     */
    private final FilmService filmService;

    /**
     * Эндпоинт GET /films.
     *
     * @return список фильмов.
     */
    @GetMapping("/films")
    public Collection<Film> getFilms() {
        log.info("GET /films");
        return filmService.getAll();
    }

    @GetMapping("films/{id}")
    public Film getFilm(@PathVariable int id) {
        log.info("GET /film/" + id);
        return filmService.get(id);
    }

    /**
     * Эндпоинт POST /films.
     *
     * @param film тело запроса
     * @return созданный объект фильма в случае успеха.
     */
    @PostMapping("/films")
    @Validated({Marker.OnCreate.class})
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("POST /films: получен для " + film);
        filmService.create(film);
        log.info("POST /films: " + film);
        return film;
    }

    /**
     * Эндпоинт PUT /films
     *
     * @param film тело запроса
     * @return обновленный объект фильма в случае успеха
     */
    @PutMapping("/films")
    @Validated({Marker.OnUpdate.class})
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("PUT /films: получен для " + film.getId());
        filmService.update(film);
        log.info("PUT /films: " + film.getId());
        return film;
    }

    @PutMapping("/films/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        log.info("PUT /films/{id}/like/{userId}: получен для фильма с id = " + filmId + ", userId = " + userId);
        filmService.addLike(filmId, userId);
        log.info("Пользователь с id = " + userId + " лайкнул фильм с id = " + filmId + ".");
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        log.info("DELETE /films/{id}/like/{userId}: получен для фильма с id = " + filmId + ", userId = " + userId);
        filmService.deleteLike(filmId, userId);
        log.info("Пользователь с id = " + userId + " удалил лайк у фильма с id = " + filmId + ".");
    }

    @GetMapping("/films/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("GET /films/popular: получен для count = " + count);
        Collection<Film> popularFilms = filmService.getPopulars(count);
        log.info("Возвращен список популярных фильмов: " + popularFilms);
        return popularFilms;
    }

}
