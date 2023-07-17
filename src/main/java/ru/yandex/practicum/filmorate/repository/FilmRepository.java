package ru.yandex.practicum.filmorate.repository;

import org.springframework.lang.Nullable;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс, описывающий репозиторий фильмов.
 */
public interface FilmRepository extends ModelRepository<Film> {
    /**
     * Добавление лайка фильму.
     *
     * @param filmId фильм, которому добавляется лайк.
     * @param userId пользователь, который добавляет лайк.
     */
    void addLike(int filmId, int userId);

    /**
     * Удаление лайка у фильма пользователем.
     *
     * @param filmId фильм, у которого удаляется лайк.
     * @param userId пользователь, который удаляет лайк.
     */
    void deleteLike(int filmId, int userId);

    /**
     * Получение списка популярных фильмов. Популярность фильма определяет число лайков.
     *
     * @param count сколько наиболее популярных фильмов необходимо вернуть.
     * @return список популярных фильмов.
     */
    List<Film> getPopularsFilms(int count);

    /**
     * Получение списка доступных жанров, которые могут быть у фильма.
     *
     * @return список доступных жанров.
     */
    List<Film.Genre> getTotalAvailableGenres();

    /**
     * Получить список жанров фильма.
     *
     * @param film фильм, список жанров которых требуется.
     * @return список жанров у фильма
     */
    List<Film.Genre> getFilmGenres(Film film);

    /**
     * Получение списка всех доступных возврастных рейтингов.
     *
     * @return список доступных MPA или null.
     */
    @Nullable
    List<Film.MotionPictureAssociation> getTotalAvailableMpa();

    /**
     * Получение MPA по id.
     *
     * @param id id трубемого MPA
     * @return MPA соответствующий id или null.
     */
    @Nullable
    Optional<Film.MotionPictureAssociation> getMpaById(int id);

    /**
     * Получение сущности жанра по его id.
     *
     * @param id id запрашиваемого жанра.
     * @return сущность запрашиваемого жанра или null.
     */
    @Nullable
    Optional<Film.Genre> getGenreById(int id);
}
