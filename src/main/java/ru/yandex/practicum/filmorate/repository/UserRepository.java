package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

/**
 * Интерфейс, описывающий репозиторий пользователей.
 */
public interface UserRepository extends ModelRepository<User> {
    /**
     * Добавление пользователей в друзья.
     *
     * @param id       пользователь, который добавляет в друзья другого пользователя.
     * @param friendId пользователь, которого добавляют в друзья.
     */
    void addToFriends(Integer id, Integer friendId);

    /**
     * Удаление пользователя из друзей.
     *
     * @param id       пользователь, который удаляет другого пользователя из друзей.
     * @param friendId удаляемый из друзей пользователь.
     */
    void deleteFromFriends(Integer id, Integer friendId);

    /**
     * Получение списка друзей пользователя.
     *
     * @param id пользователь, список друзей которого запрашивается.
     * @return список друзей.
     */
    List<User> getFriends(Integer id);

    /**
     * Получение списка подтвержденных друзей.
     *
     * @param id пользователь, список друзей которого запрашивается.
     * @return список подтвержденных друзей.
     */
    List<User> getConfirmFriends(Integer id);
}
