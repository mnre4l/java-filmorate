package ru.yandex.practicum.filmorate.service;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=NotBeforeMovieDayValidator.class)
public @interface NotBeforeMovieDay {
    String message() default "Bad release day";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
