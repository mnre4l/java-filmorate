package ru.yandex.practicum.filmorate.repository.user.in_memory;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;


/**
 * Класс предназначен для хранения сущностей-пользователей в памяти.
 */
@Component("InMemoryUsersRepository")
public class InMemoryUsersRepository implements UserRepository {
    /**
     * Хеш-таблица, предназначенная для хранения пользователей по паре id пользователя - пользователь.
     */
    private final HashMap<Integer, User> repository = new HashMap<>();
    /**
     * Хеш-таблица, предназначенная для хранения друзей пользователя.
     * Ключ - id пользователя, значение - множество друзей пользователя.
     * Упорядочивание пользователей в множестве друзей происходит по значению их id.
     */
    private final HashMap<Integer, TreeSet<User>> friendsRepository = new HashMap<>();
    /**
     * Инкрементируемое значение, предназначенное для установки id пользователей.
     */
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
    public void delete(User user) {
        repository.remove(user.getId());
    }

    @Override
    public List<User> getAll() {
        return List.copyOf(repository.values());
    }

    @Override
    public List<Integer> getAllIds() {
        return List.copyOf(repository.keySet());
    }

    @Override
    public List<User> getFriends(Integer id) {
        return List.copyOf(friendsRepository.get(id));
    }

    /**
     * @param id
     * @return
     */
    @Override
    public List<User> getConfirmFriends(Integer id) {
        throw new NotYetImplementedException("Не реализовано для репозитория в памяти");
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
    public Optional<User> get(Integer id) {
        return Optional.of(repository.get(id));
    }

    @Override
    public void deleteAll() {
        repository.clear();
        friendsRepository.clear();
    }
}
