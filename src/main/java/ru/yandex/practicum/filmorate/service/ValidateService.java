package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс предназначен для валидации сущностей и запросов к контроллерам.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ValidateService {
    @Qualifier("DbFilmsRepository")
    private final FilmRepository filmRepository;
    @Qualifier("DbUsersRepository")
    private final UserRepository userRepository;

    /**
     * Проверка, что фильм уже создан.
     *
     * @param id идентификатор фильма.
     */
    public void isFilmCreated(Integer id) {
        if (filmRepository.get(id).isEmpty()) {
            throw new NotFoundException("Фильм с id = " + id + " не добавлен.");
        }
    }

    /**
     * Проверка, что пользователь создан.
     *
     * @param id идентификатор пользователя.
     */
    public void isUserCreated(Integer id) {
        if (userRepository.get(id).isEmpty()) {
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
            log.info("Установлен логин в качестве имени пользователю: " + user);
        }
    }

    /**
     * Проверка на существование запроса на друзья от одного пользователя другому.
     *
     * @param id       пользователь, наличие запроса от которого проверяется.
     * @param friendId пользователь, наличие запроса которому проверяется.
     */
    public void checkThereIsFriendRequestFromIdToFriendId(Integer id, Integer friendId) {
        if (!userRepository.getFriends(id).contains(userRepository.get(friendId).get())) {
            throw new FriendsException("Пользователь с id = " + id + " не оставлял заявку в друзья" +
                    "пользователю id = " + friendId);
        }
    }

    /**
     * Проверка на отсутствие запроса на друзья от одного пользователя к другому.
     *
     * @param id       пользователь, наличие запроса от которого проверяется.
     * @param friendId пользователь, наличие запроса которому проверяется.
     */
    public void checkNoFriendRequestFromIdToFriendId(Integer id, Integer friendId) {
        if (userRepository.getFriends(id).contains(userRepository.get(friendId).get())) {
            throw new FriendsException("Пользователь с id = " + id + " уже оставил заявку " +
                    "в друзья пользователю id = " + friendId);
        }
    }

    /**
     * Проверка, что жанры с пришедшими id существуют и установка их в поле к фильму.
     *
     * @param film объект фильма, в поле к которому установятся жанры.
     */
    public void checkAndSetGenres(Film film) {
        /*
         если в пришедшем json не были указаны жанры
         */
        if (film.getGenres() == null) {
            film.setGenres(new LinkedHashSet<>());
            return;
        }

        /*
         * Хеш-таблица вида:
         * Ключ - id известного жанра
         * Значение - название известного жанра, соответствующего id
         */
        Map<Integer, String> totalGenres = filmRepository.getTotalAvailableGenres().stream()
                .collect(Collectors.toMap(genre -> genre.getId(), genre -> genre.getName()));
        /*
           Список id жанров у пришедшего фильма
         */
        List<Integer> filmGenresIds = film.getGenres().stream()
                .map(genre -> genre.getId())
                .collect(Collectors.toList());
        LinkedHashSet<Film.Genre> genres = new LinkedHashSet<>();

        for (Integer genreId : filmGenresIds) {
            if (!totalGenres.keySet().contains(genreId))
                throw new NotFoundException("Не добавлен жанр с id = " + genreId);

            Film.Genre genre = new Film.Genre();

            genre.setId(genreId);
            genre.setName(totalGenres.get(genreId));
            genres.add(genre);
        }
        film.setGenres(genres);
    }

    /**
     * Проверка, что MPA с таким id существует и установка в поле к фильму MPA.
     *
     * @param film фильм, в поле к которому устанавливается MPA.
     */
    public void checkAndSetMpa(Film film) {
        Map<Integer, String> totalMpa = filmRepository.getTotalAvailableMpa().stream()
                .collect(Collectors.toMap(mpa -> mpa.getId(), mpa -> mpa.getName()));
        int filmMpaId = film.getMpa().getId();

        if (!totalMpa.keySet().contains(filmMpaId))
            throw new NotFoundException("Не найден MPA с " + "id = " + filmMpaId);
        film.getMpa().setName(totalMpa.get(filmMpaId));
    }


}
