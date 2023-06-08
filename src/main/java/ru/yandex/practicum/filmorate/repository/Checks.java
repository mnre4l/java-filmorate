package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidationException;
import java.util.HashMap;

/**
 * Класс предназначен для обработки некоторых полей пришедшего Json-объекта в контроллеры
 */
public class Checks {

    /**
     * Проверка, что объект уже создан. Предназначена для методов обновления.
     * @param repository репозиторий, в котором обновляется сущность
     * @param id идентификатор сущности
     * @param <T> тип сущности
     */
    public static <T> void isCreated(HashMap<Integer, T> repository, Integer id) {
        if (!repository.containsKey(id)) {
            throw new ValidationException(Object.class + " с id = " + id + " не добавлен.");
        }
    }

    /**
     * Обработка случая name = null для классов пользователей.
     * @param user объект пользователя, сохраняемый в репозиторий пользователей
     */
    public static void isNameNull(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}
