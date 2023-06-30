package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UsersAreNotFriendsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

/**
 * Класс предназначен для обработки некоторых полей пришедшего Json-объекта в контроллеры
 */
@Service
@RequiredArgsConstructor
public class ValidateService {
    private final FilmRepository filmRepository;
    @Qualifier("InMemoryUsersRepository")
    private final UserRepository userRepository;

    /**
     * Проверка, что объект уже создан. Предназначена для методов обновления.
     *
     * @param id идентификатор сущности
     */
    public void isFilmCreated(Integer id) {
        if (!filmRepository.getAllId().contains(id)) {
            throw new NotFoundException("Фильм с id = " + id + " не добавлен.");
        }
    }

    public void isUserCreated(Integer id) {
        if (!userRepository.getAllId().contains(id)) {
            throw new NotFoundException("Пользователь с id = " + id + " не добавлен.");
        }
    }

    /**
     * Обработка случая name = null для классов пользователей.
     *
     * @param user объект пользователя, сохраняемый в репозиторий пользователей
     */
    public void setNameAsLoginIfNameNull(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public void areUsersFriends(Integer id, Integer friendId) {
        if (!userRepository.getFriends(id).contains(userRepository.get(friendId))) {
            throw new UsersAreNotFriendsException("Пользователи не являются друзьями");
        }
    }
}
