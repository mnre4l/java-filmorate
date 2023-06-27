package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmRepository repository;
    @Qualifier("InMemoryFilmsRepository")
    private final ValidateService validateService;

    public Collection<Film> getAll() {
        return repository.getAll();
    }

    public void create(Film film) {
        repository.create(film);
    }

    public void update(Film film) {
        validateService.isFilmCreated(film.getId());
        repository.update(film);
    }

    public void addLike(int filmId, int userId) {
        validateService.isFilmCreated(filmId);
        validateService.isUserCreated(userId);
        repository.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        validateService.isFilmCreated(filmId);
        validateService.isUserCreated(userId);
        repository.deleteLike(filmId, userId);
    }

    public Collection<Film> getPopulars(int count) {
        return repository.getPopulars(count);
    }

    public Film get(int id) {
        validateService.isFilmCreated(id);
        return repository.get(id);
    }
}
