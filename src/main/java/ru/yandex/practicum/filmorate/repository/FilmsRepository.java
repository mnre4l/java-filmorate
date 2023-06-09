package ru.yandex.practicum.filmorate.repository;

import jdk.jshell.spi.ExecutionControl;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;


/**
 * Класс предназначен для хранения сущностей-фильмов.
 */
@Component
public class FilmsRepository implements ModelsRepository<Film> {
    private final HashMap<Integer, Film> repository = new HashMap<>();
    private int id;

    @Override
    public void create(Film film) {
        film.setId(++id);
        repository.put(film.getId(), film);
    }

    @Override
    public void update(Film film) {
        Checks.isCreated(repository, film.getId());
        repository.put(film.getId(), film);
    }

    @Override
    public void delete(Film film) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Не реализовано");
    }

    @Override
    public Film get(int id) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Не реализовано");
    }

    @Override
    public List<Film> getAll() {
        return List.copyOf(repository.values());
    }
}
