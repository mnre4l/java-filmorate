package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;
import java.util.HashMap;
import java.util.List;


/**
 * Класс предназначен для хранения сущностей-пользователей.
 */
public class UsersRepository implements ModelsRepository<User> {
    private final HashMap<Integer, User> repository = new HashMap<>();
    private int id;
    @Override
    public void create(User user) {
        Checks.isNameNull(user);
        user.setId(++id);
        repository.put(user.getId(), user);
    }

    @Override
    public void update(User user) {
        Checks.isCreated(repository, user.getId());
        repository.put(user.getId(), user);
    }

    @Override
    public void delete(User user) {
    }

    @Override
    public User get(int id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return List.copyOf(repository.values());
    }
}
