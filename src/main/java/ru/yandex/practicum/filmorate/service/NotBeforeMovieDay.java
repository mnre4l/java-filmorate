package ru.yandex.practicum.filmorate.service;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Валидация даты релиза фильма.
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=NotBeforeMovieDayValidator.class)
public @interface NotBeforeMovieDay {
    String message() default "Bad release date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
