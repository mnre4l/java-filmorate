package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository extends ModelRepository<Film> {
    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Film> getPopulars(int count);
}
