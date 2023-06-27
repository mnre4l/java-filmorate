package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface UserRepository extends ModelRepository<User> {
    void addToFriends(Integer id, Integer friendId);

    void deleteFromFriends(Integer id, Integer friendId);

    Set<User> getFriends(Integer id);
}
