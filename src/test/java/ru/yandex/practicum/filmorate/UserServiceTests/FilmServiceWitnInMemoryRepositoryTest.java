package ru.yandex.practicum.filmorate.UserServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.repository.film.InMemoryFilmsRepository;
import ru.yandex.practicum.filmorate.repository.user.InMemoryUsersRepository;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmServiceWitnInMemoryRepositoryTest {
    private UserService userService;
    private FilmService filmService;
    private FilmRepository filmRepository;
    private ValidateService validateService;
    private UserRepository userRepository;
    private final User user1 = new User();
    private final User user2 = new User();
    private final Film film1 = new Film();
    private final Film film2 = new Film();

    @BeforeEach
    public void init() {
        userRepository = new InMemoryUsersRepository();
        filmRepository = new InMemoryFilmsRepository();
        validateService = new ValidateService(filmRepository, userRepository);
        userService = new UserService(userRepository, validateService);
        filmService = new FilmService(filmRepository, validateService);

        user1.setName("User name");
        user1.setEmail("email@mail.ru");
        user1.setLogin("login");
        user1.setBirthday(LocalDate.of(1996, 8, 17));

        user2.setName("User2 name");
        user2.setEmail("user2@mail.ru");
        user2.setLogin("user2login");
        user2.setBirthday(LocalDate.of(1995, 8, 17));

        userService.create(user1);
        userService.create(user2);

        film1.setName("Film1 name");
        film1.setDuration(50);
        film1.setReleaseDate(LocalDate.of(2000, 5, 22));
        film1.setDescription("Description1");

        film2.setName("Film2 name");
        film2.setDuration(520);
        film2.setReleaseDate(LocalDate.of(2002, 5, 22));
        film2.setDescription("Description2");

        filmService.create(film1);
        filmService.create(film2);
    }

    @Test
    public void shouldAddLikeToFilm() {
        filmService.addLike(film1.getId(), user1.getId());
        filmService.addLike(film1.getId(), user2.getId());
        assertEquals(filmRepository.getPopulars(1), List.of(film1));
    }

    @Test
    public void shouldDeleteLikeFromFilm() {
        shouldAddLikeToFilm();
        filmService.deleteLike(film1.getId(), user1.getId());
        filmService.deleteLike(film1.getId(), user2.getId());
        filmService.addLike(film2.getId(), user1.getId());
        assertEquals(filmRepository.getPopulars(1), List.of(film2));
    }

    @Test
    public void shouldReturnPopularsFilm() {
        filmService.addLike(film2.getId(), user1.getId());
        filmService.addLike(film2.getId(), user2.getId());
        filmService.addLike(film1.getId(), user2.getId());

        LinkedHashSet<Film> correctPopularsFilmList = new LinkedHashSet<>();
        correctPopularsFilmList.add(film2);
        correctPopularsFilmList.add(film1);

        assertEquals(correctPopularsFilmList, new LinkedHashSet<>(filmService.getPopulars(2)));
    }
}
