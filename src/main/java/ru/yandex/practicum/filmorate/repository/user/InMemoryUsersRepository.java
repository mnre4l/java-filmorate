package ru.yandex.practicum.filmorate.repository.user;

import jdk.jshell.spi.ExecutionControl;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;


/**
 * Класс предназначен для хранения сущностей-пользователей.
 */
@Component("InMemoryUsersRepository")
public class InMemoryUsersRepository implements UserRepository {
    private final HashMap<Integer, User> repository = new HashMap<>();
    private final HashMap<Integer, Set<User>> friendsRepository = new HashMap<>();
    private int id;

    @Override
    public void create(User user) {
        user.setId(++id);
        repository.put(user.getId(), user);
        friendsRepository.put(user.getId(), new HashSet<>());
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
    public Collection<User> getAll() {
        return repository.values();
    }

    @Override
    public Collection<Integer> getAllId() {
        return repository.keySet();
    }

    @Override
    public TreeSet<User> getFriends(Integer id) {
        //если вернуть не в порядке возрастания id, постман считает, что тест провален ¯\_(ツ)_/¯
        TreeSet<User> friends = new TreeSet<>(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getId() - o2.getId();
            }
        });
        friends.addAll(friendsRepository.get(id));
        return friends;
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
