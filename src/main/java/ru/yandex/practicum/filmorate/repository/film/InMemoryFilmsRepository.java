package ru.yandex.practicum.filmorate.repository.film;

import jdk.jshell.spi.ExecutionControl;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Класс предназначен для хранения сущностей-фильмов.
 */
@Component("InMemoryFilmsRepository")
public class InMemoryFilmsRepository implements FilmRepository {
    private final HashMap<Integer, Film> repository = new HashMap<>();
    private final HashMap<Integer, Set<Integer>> filmAndUsersWhoLikedRepository = new HashMap<>();
    private final TreeSet<Integer> popular = new TreeSet<>(new Comparator<Integer>() {
        @Override
        public int compare(Integer filmId1, Integer filmId2) {
            if (filmId1 == filmId2) return 0;

            int filmLikes1 = filmAndUsersWhoLikedRepository.get(filmId1).size();
            int filmLikes2 = filmAndUsersWhoLikedRepository.get(filmId2).size();

            if (filmLikes1 == filmLikes2) return 1;
            return filmLikes2 - filmLikes1;
        }
    });
    private int id;

    @Override
    public void create(Film film) {
        film.setId(++id);
        repository.put(film.getId(), film);
        filmAndUsersWhoLikedRepository.put(film.getId(), new HashSet<>());
        popular.add(film.getId());
    }

    @Override
    public void update(Film film) {
        repository.put(film.getId(), film);
    }

    @Override
    public void delete(Film film) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Не реализовано");
    }

    @Override
    public Collection<Film> getAll() {
        return repository.values();
    }

    @Override
    public Collection<Integer> getAllId() {
        return repository.keySet();
    }

    @Override
    public Film get(Integer id) {
        return repository.get(id);
    }

    @Override
    public void deleteAll() {
        repository.clear();
    }

    @Override
    public void addLike(int filmId, int userId) {
        popular.remove(filmId);
        filmAndUsersWhoLikedRepository.get(filmId).add(userId);
        popular.add(filmId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        popular.remove(filmId);
        filmAndUsersWhoLikedRepository.get(filmId).remove(userId);
        popular.add(filmId);
    }

    public Collection<Film> getPopulars(int count) {
        return popular.stream()
                .limit(count)
                .map(id -> repository.get(id))
                .collect(Collectors.toList());
    }
}
