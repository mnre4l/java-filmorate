package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    @Qualifier("InMemoryFilmsRepository")
    @NonNull
    private final FilmRepository repository;
    private final ValidateService validateService;

    public List<Film> getAll() {
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

    public List<Film> getPopulars(int count) {
        return repository.getPopulars(count);
    }

    public Film get(int id) {
        validateService.isFilmCreated(id);
        return repository.get(id);
    }
}
