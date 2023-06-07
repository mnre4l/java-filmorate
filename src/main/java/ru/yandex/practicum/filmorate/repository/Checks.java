package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidationException;
import java.util.HashMap;


public class Checks {
    public static <T> void isCreated(HashMap<Integer, T> repository, Integer id) {
        if (!repository.containsKey(id)) {
            throw new ValidationException(Object.class + " с id = " + id + " не добавлен.");
        }
    }

    public static void isNameNull(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}
