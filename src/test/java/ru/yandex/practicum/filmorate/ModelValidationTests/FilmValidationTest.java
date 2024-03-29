package ru.yandex.practicum.filmorate.ModelValidationTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.validation.Marker;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmValidationTest {
    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    Film film;

    @BeforeEach
    public void createCorrectFilmObject() {
        film = new Film();
        film.setName("Film name");
        film.setDuration(50);
        film.setReleaseDate(LocalDate.of(2000, 5, 22));
        film.setDescription("Description");

        Film.MotionPictureAssociation mpa = new Film.MotionPictureAssociation();
        mpa.setId(1);

        film.setMpa(mpa);
    }

    @Test
    public void shouldCreateFilmDefault() {
        Set<ConstraintViolation<Film>> validates = validator.validate(film, Marker.OnCreate.class);

        assertEquals(validates.size(), 0);
    }

    @Test
    public void shouldNotCreateFilmIfIdNotNull() {
        film.setId(5);

        Set<ConstraintViolation<Film>> validates = validator.validate(film, Marker.OnCreate.class);

        assertEquals(validates.size(), 1);
    }

    @Test
    public void shouldNotUpdateFilmWithNegativeId() {
        film.setId(-1);

        Set<ConstraintViolation<Film>> validates = validator.validate(film, Marker.OnUpdate.class);

        assertEquals(validates.size(), 1);
    }

    @Test
    public void shouldNotCreateAndUpdateFilmWithBadReleaseDate() {
        film.setReleaseDate(LocalDate.of(1800, 5, 22));

        Set<ConstraintViolation<Film>> validates = validator.validate(film);

        assertEquals(validates.size(), 1);
    }

    @Test
    public void shouldNotCreateAndUpdateFilmWithBlankName() {
        film.setName("");

        Set<ConstraintViolation<Film>> validates = validator.validate(film);

        assertEquals(validates.size(), 1);
    }

    @Test
    public void shouldNotCreateAndUpdateFilmWithNullName() {
        film.setName(null);

        Set<ConstraintViolation<Film>> validates = validator.validate(film);

        assertEquals(validates.size(), 1);
    }

    @Test
    public void shouldNotCreateAndUpdateFilmWithLongDescription() {
        film.setDescription("a".repeat(201));

        Set<ConstraintViolation<Film>> validates = validator.validate(film);

        assertEquals(validates.size(), 1);
    }

    @Test
    public void shouldNotCreateAndUpdateFilmWithNegativeDuration() {
        film.setDuration(-1);

        Set<ConstraintViolation<Film>> validates = validator.validate(film);

        assertEquals(validates.size(), 1);
    }
}
