package ru.yandex.practicum.filmorate.repository.user;

import jdk.jshell.spi.ExecutionControl;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;


/**
 * Класс предназначен для хранения сущностей-пользователей.
 */
@Component("InMemoryUsersRepository")
public class InMemoryUsersRepository implements UserRepository {
    private final HashMap<Integer, User> repository = new HashMap<>();
    private final HashMap<Integer, TreeSet<User>> friendsRepository = new HashMap<>();
    private int id;

    @Override
    public void create(User user) {
        user.setId(++id);
        repository.put(user.getId(), user);
        friendsRepository.put(user.getId(), new TreeSet<>(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.getId() == o2.getId()) return 0;
                return o1.getId() - o2.getId();
            }
        }));
    }

    @Override
    public void update(User user) {
        repository.put(user.getId(), user);
    }

    @Override
    public void delete(User user) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Не реализовано");
    }

    @Override
    public List<User> getAll() {
        return List.copyOf(repository.values());
    }

    @Override
    public List<Integer> getAllId() {
        return List.copyOf(repository.keySet());
    }

    @Override
    public List<User> getFriends(Integer id) {
        return List.copyOf(friendsRepository.get(id));
    }

    @Override
    public void addToFriends(Integer id, Integer friendId) {
        friendsRepository.get(id).add(repository.get(friendId));
        friendsRepository.get(friendId).add(repository.get(id));
    }

    @Override
    public void deleteFromFriends(Integer id, Integer friendId) {
        friendsRepository.get(id).remove(repository.get(friendId));
        friendsRepository.get(friendId).remove(repository.get(id));
    }

    @Override
    public User get(Integer id) {
        return repository.get(id);
    }

    @Override
    public void deleteAll() {
        repository.clear();
        friendsRepository.clear();
    }
}
