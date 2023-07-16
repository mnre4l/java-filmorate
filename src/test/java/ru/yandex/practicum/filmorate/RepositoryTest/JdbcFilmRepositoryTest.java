package ru.yandex.practicum.filmorate.RepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.film.dao.DbFilmsRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(DbFilmsRepository.class)
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JdbcFilmRepositoryTest {
    @Autowired
    @Qualifier("DbFilmsRepository")
    private FilmRepository filmRepository;

    @Test
    public void shouldFindTestFilmById() {
        Film film = filmRepository.get(1).get();

        assertNotNull(film);

        assertEquals("test film name", film.getName());
        assertEquals("test film description", film.getDescription());
        assertEquals(LocalDate.of(2012, 11, 15), film.getReleaseDate());
        assertEquals(90, film.getDuration());
        assertEquals(2, film.getRate());
        assertEquals(2, film.getMpa().getId());
        assertEquals(1, film.getId());
    }

    @Test
    public void shouldNotFindTestFilmWithBadId() {
        assertEquals(Optional.empty(), filmRepository.get(9999));
    }

    @Test
    public void shouldCreateFilm() {
        Film film = new Film();

        film.setName("test2");
        film.setDescription("test descr2");
        film.setRate(2);
        film.setDuration(15);
        film.setReleaseDate(LocalDate.of(2010, 5, 1));

        Film.MotionPictureAssociation mpa = new Film.MotionPictureAssociation();

        mpa.setId(2);
        film.setMpa(mpa);
        filmRepository.create(film);

        Film createdFilm = filmRepository.get(2).get();

        assertEquals(film.getName(), createdFilm.getName());
        assertEquals(film.getDescription(), createdFilm.getDescription());
        assertEquals(film.getReleaseDate(), createdFilm.getReleaseDate());
        assertEquals(film.getDuration(), createdFilm.getDuration());
        assertEquals(film.getRate(), createdFilm.getRate());
        assertEquals(film.getMpa().getId(), createdFilm.getMpa().getId());
    }

    @Test
    public void shouldUpdateTestFilm() {
        Film film = filmRepository.get(1).get();

        film.setName("new name");
        assertNotEquals(film.getName(), filmRepository.get(1).get().getName());
        filmRepository.update(film);
        assertEquals(film.getName(), filmRepository.get(1).get().getName());
    }

    @Test
    public void shouldReturnAllFilms() {
        Film film = new Film();

        film.setName("test2");
        film.setDescription("test descr2");
        film.setRate(2);
        film.setDuration(15);
        film.setReleaseDate(LocalDate.of(2010, 5, 1));

        Film.MotionPictureAssociation mpa = new Film.MotionPictureAssociation();

        mpa.setId(2);
        film.setMpa(mpa);
        filmRepository.create(film);

        assertEquals(List.of(filmRepository.get(1).get(), filmRepository.get(2).get()), filmRepository.getAll());
    }

    @Test
    public void shouldReturnAllGenres() {
        List<Film.Genre> genres = filmRepository.getTotalAvailableGenres();

        for (Film.Genre genre : genres) {
            assertInstanceOf(Film.Genre.class, genre);
            assertNotNull(genre.getId());
            assertNotNull(genre.getName());
        }
    }

    @Test
    public void shouldReturnGenreById() {
        Film.Genre genre = filmRepository.getGenreById(1).get();

        assertInstanceOf(Film.Genre.class, genre);
        assertNotNull(genre.getId());
        assertNotNull(genre.getName());
    }

    @Test
    public void shouldReturnMpaById() {
        Film.MotionPictureAssociation mpa = filmRepository.getMpaById(1).get();

        assertInstanceOf(Film.MotionPictureAssociation.class, mpa);
        assertNotNull(mpa.getId());
        assertNotNull(mpa.getName());
    }

    @Test
    public void shouldReturnAllAvailableMpa() {
        List<Film.MotionPictureAssociation> allMpa = filmRepository.getTotalAvailableMpa();

        for (Film.MotionPictureAssociation mpa : allMpa) {
            assertInstanceOf(Film.MotionPictureAssociation.class, mpa);
            assertNotNull(mpa.getId());
            assertNotNull(mpa.getName());
        }
    }

    @Test
    public void shouldAddUserLike() {
        Film badFilm = new Film();

        badFilm.setName("bad Film");
        badFilm.setDescription("bad Film description");
        badFilm.setRate(2);
        badFilm.setDuration(15);
        badFilm.setReleaseDate(LocalDate.of(2010, 5, 1));

        Film.MotionPictureAssociation mpa = new Film.MotionPictureAssociation();
        mpa.setId(1);
        badFilm.setMpa(mpa);

        filmRepository.create(badFilm);
        filmRepository.addLike(1, 1);
        assertEquals(List.of(filmRepository.get(1).get()), filmRepository.getPopularsFilms(1));
    }

    @Test
    public void shouldDeleteLike() {
        shouldAddUserLike();
        filmRepository.deleteLike(1, 1);
        filmRepository.addLike(2, 1);
        assertEquals(List.of(filmRepository.get(2).get()), filmRepository.getPopularsFilms(1));
    }
}
