package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.service.Marker;
import ru.yandex.practicum.filmorate.service.NotBeforeMovieDay;
import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * Класс описывает фильм, добавляемый в сервис.
 */
@Data
public class Film {

    /**
     * Идентификатор фильма.
     */
    @Digits(integer = Integer.MAX_VALUE, fraction = 0, groups = Marker.OnUpdate.class)
    @Positive(groups = Marker.OnUpdate.class)
    @Null(groups = Marker.OnCreate.class)
    @NotNull(groups = Marker.OnUpdate.class)
    private Integer id;
    /**
     * Имя фильма.
     */
    @NotBlank
    private String name;
    /**
     * Описание фильма.
     */
    @Size(max = 200)
    private String description;
    /**
     * Дата релиза.
     */
    @NotBeforeMovieDay
    private LocalDate releaseDate;
    /**
     * Продолжительность фильма в минутах.
     */
    @Digits(integer = Integer.MAX_VALUE, fraction = 0)
    @Positive
    private int duration;
    /**
     * Ограничение по дате релиза.
     */
    public static final LocalDate FIRST_COMMERCIAL_MOVIE_DAY = LocalDate.parse("1895-12-28");
}
