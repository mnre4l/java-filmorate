package ru.yandex.practicum.filmorate.repository.film.in_memory;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Класс предназначен для хранения сущностей-фильмов в памяти.
 */
@Component("InMemoryFilmsRepository")
public class InMemoryFilmsRepository implements FilmRepository {
    /**
     * Хеш-таблица, хранящая созданные фильмы.
     * Ключ - id фильма, значение - фильм.
     */
    private final HashMap<Integer, Film> repository = new HashMap<>();
    /**
     * Хеш-таблица, хранящая лайки пользователей.
     * Ключ - id пользователя.
     * Значение - фильмы, которым пользователь поставил лайк.
     */
    private final HashMap<Integer, Set<Integer>> filmAndUsersWhoLikedRepository = new HashMap<>();
    /**
     * Множество, хранящее наиболее популярные фильмы.
     */
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
    /**
     * Инкрементируемый id фильмов.
     */
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
    public void delete(Film film) {
        repository.remove(film.getId());
    }

    @Override
    public List<Film> getAll() {
        return List.copyOf(repository.values());
    }

    @Override
    public List<Integer> getAllIds() {
        return List.copyOf(repository.keySet());
    }

    @Override
    public Optional<Film> get(Integer id) {
        return Optional.of(repository.get(id));
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

    @Override
    public List<Film.Genre> getTotalAvailableGenres() {
        throw new NotYetImplementedException("Не реализовано для репозитория в памяти");
    }

    @Override
    public List<Film.Genre> getFilmGenres(Film film) {
        throw new NotYetImplementedException("Не реализовано для репозитория в памяти");
    }


    @Override
    public List<Film.MotionPictureAssociation> getTotalAvailableMpa() {
        throw new NotYetImplementedException("Не реализовано для репозитория в памяти");
    }


    @Override
    public Optional<Film.MotionPictureAssociation> getMpaById(int id) {
        throw new NotYetImplementedException("Не реализовано для репозитория в памяти");
    }


    @Override
    public Optional<Film.Genre> getGenreById(int id) {
        throw new NotYetImplementedException("Не реализовано для репозитория в памяти");
    }


    public List<Film> getPopularsFilms(int count) {
        return popular.stream()
                .limit(count)
                .map(id -> repository.get(id))
                .collect(Collectors.toList());
    }
}
