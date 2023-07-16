package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.validation.Marker;

import javax.validation.Valid;
import java.util.List;


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
    public List<Film> getFilms() {
        log.info("GET /films");
        return filmService.getAll();
    }

    /**
     * Эндпоинт GET/fims/{id}
     *
     * @param id id фильма.
     * @return запрашиваемый фильм.
     */
    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable int id) {
        log.info("GET /film/" + id);
        return filmService.get(id);
    }

    /**
     * Эндпоинт GET /mpa.
     *
     * @return все типы MPA (в виде объектов), присутствущие в репозитории.
     */
    @GetMapping("/mpa")
    public List<Film.MotionPictureAssociation> getAllMpa() {
        log.info("GET /mpa/");
        return filmService.getAllMpa();
    }

    /**
     * Эндпоинт /mpa/{id}
     *
     * @param id id MPA.
     * @return объект MPA, соответствующий id.
     */
    @GetMapping("/mpa/{id}")
    public Film.MotionPictureAssociation getMpa(@PathVariable("id") int id) {
        log.info("GET /mpa/" + id);
        return filmService.getMpa(id);
    }

    /**
     * Эндпоинт /genres.
     *
     * @return список всех жанров, содержащихся в репозитории.
     */
    @GetMapping("/genres")
    public List<Film.Genre> getAllGenres() {
        log.info("GET /genre/");
        return filmService.getAllGenres();
    }

    /**
     * Эндпоинт /genres/{id}
     *
     * @param id id жанро.
     * @return объект жанра фильма, соответсвующий id.
     */
    @GetMapping("/genres/{id}")
    public Film.Genre getGenre(@PathVariable("id") int id) {
        log.info("GET /genre/" + id);
        return filmService.getGenre(id);
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

    /**
     * Эндпоинт /films/{id}/like/{userId}. Предназначен для обработки запроса на добавление лайка к фильму
     * от пользователей.
     *
     * @param filmId id фильма, которому пользователь ставит лайк.
     * @param userId id пользователя, который ставит лайк фильму.
     */
    @PutMapping("/films/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        log.info("PUT /films/{id}/like/{userId}: получен для фильма с id = " + filmId + ", userId = " + userId);
        filmService.addLike(filmId, userId);
        log.info("Пользователь с id = " + userId + " лайкнул фильм с id = " + filmId + ".");
    }

    /**
     * Эндпоинт /films/{id}/like/{userId}. Предназначен для удаления лайка у фильма от пользователя.
     *
     * @param filmId id фильма, лайк у которого удалится.
     * @param userId id пользователя, который удаляет свой лайк.
     */
    @DeleteMapping("/films/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        log.info("DELETE /films/{id}/like/{userId}: получен для фильма с id = " + filmId + ", userId = " + userId);
        filmService.deleteLike(filmId, userId);
        log.info("Пользователь с id = " + userId + " удалил лайк у фильма с id = " + filmId + ".");
    }

    /**
     * Эндпоинт /films/popular. Предназначен для получения списка наиболее популярных фильмов.
     *
     * @param count число фильмов, которые будут включены в возвращаемый список.
     * @return указанное число наиболее популярных фильмов фильмов.
     */
    @GetMapping("/films/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("GET /films/popular: получен для count = " + count);
        List<Film> popularFilms = filmService.getPopulars(count);
        log.info("Возвращен список популярных фильмов: " + popularFilms);
        return popularFilms;
    }
}
