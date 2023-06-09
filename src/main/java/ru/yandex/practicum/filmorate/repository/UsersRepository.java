package ru.yandex.practicum.filmorate.repository;

import jdk.jshell.spi.ExecutionControl;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;


/**
 * Класс предназначен для хранения сущностей-пользователей.
 */
@Component
public class UsersRepository implements ModelsRepository<User> {
    private final HashMap<Integer, User> repository = new HashMap<>();
    private int id;

    @Override
    public void create(User user) {
        Checks.setNameAsLoginIfNameNull(user);
        user.setId(++id);
        repository.put(user.getId(), user);
    }

    @Override
    public void update(User user) {
        Checks.isCreated(repository, user.getId());
        repository.put(user.getId(), user);
    }

    @Override
    public void delete(User user) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Не реализовано");
    }

    @Override
    public User get(int id) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Не реализовано");
    }

    @Override
    public List<User> getAll() {
        return List.copyOf(repository.values());
    }
}
