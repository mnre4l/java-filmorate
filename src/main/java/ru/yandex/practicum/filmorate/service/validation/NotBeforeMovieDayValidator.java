package ru.yandex.practicum.filmorate.service.validation;

import ru.yandex.practicum.filmorate.model.film.Film;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;


/**
 * Валидация даты релиза фильма.
 */
public class NotBeforeMovieDayValidator implements ConstraintValidator<NotBeforeMovieDay, LocalDate> {
    @Override
    public boolean isValid(LocalDate releaseDay, ConstraintValidatorContext constraintValidatorContext) {
        return releaseDay.isAfter(Film.FIRST_COMMERCIAL_MOVIE_DAY);
    }
}
