package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    @Qualifier("DbFilmsRepository")
    @NonNull
    private final FilmRepository repository;
    @NonNull
    private final ValidateService validateService;

    public List<Film> getAll() {
        return repository.getAll();
    }

    public void create(Film film) {
        validateService.checkAndSetGenres(film);
        validateService.checkAndSetMpa(film);
        repository.create(film);
    }

    public void update(Film film) {
        validateService.isFilmCreated(film.getId());
        validateService.checkAndSetGenres(film);
        validateService.checkAndSetMpa(film);
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
        return repository.getPopularsFilms(count);
    }

    public Film get(int id) {
        Film film = repository.get(id)
                .orElseThrow(() -> new NotFoundException("Не найден фильм с id = " + id));

        return film;
    }

    public Film.MotionPictureAssociation getMpa(int id) {
        Film.MotionPictureAssociation mpa = repository.getMpaById(id)
                .orElseThrow(() -> new NotFoundException("Не найден mpa id = " + id));

        return mpa;
    }

    public List<Film.MotionPictureAssociation> getAllMpa() {
        return repository.getTotalAvailableMpa();
    }

    public List<Film.Genre> getAllGenres() {
        return repository.getTotalAvailableGenres();
    }

    public Film.Genre getGenre(int id) {
        Film.Genre genre = repository.getGenreById(id)
                .orElseThrow(() -> new NotFoundException("Не найден genre id = " + id));

        return genre;
    }
}
