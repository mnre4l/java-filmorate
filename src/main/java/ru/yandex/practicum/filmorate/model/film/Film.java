package ru.yandex.practicum.filmorate.model.film;

import lombok.Data;
import ru.yandex.practicum.filmorate.service.validation.Marker;
import ru.yandex.practicum.filmorate.service.validation.NotBeforeMovieDay;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;

/**
 * Класс описывает фильм, добавляемый в сервис.
 */
@Data
public class Film {

    /**
     * Ограничение по дате релиза - дата релиза фильма не раньше этой даты.
     */
    public static final LocalDate FIRST_COMMERCIAL_MOVIE_DAY = LocalDate.parse("1895-12-28");

    /**
     * Идентификатор фильма.
     */
    @Digits(integer = Integer.MAX_VALUE, fraction = 0, groups = Marker.OnUpdate.class)
    @Positive(groups = Marker.OnUpdate.class)
    @Null(groups = Marker.OnCreate.class)
    @NotNull(groups = Marker.OnUpdate.class)
    private Integer id;
    /**
     * Название фильма.
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
     * Жанр фильма. Может быть несколько.
     */
    private LinkedHashSet<Genre> genres;
    /**
     * Возрастной рейтинг.
     *
     * @see <a href="../resources/data.sql">data.sql</a>
     */
    @NotNull(groups = Marker.OnCreate.class)
    private MotionPictureAssociation mpa;
    /**
     * Рейтинг.
     */
    private int rate;

    /**
     * Модель MPA
     */
    @Data
    public static class MotionPictureAssociation {
        /**
         * id MPA
         */
        @NotNull
        private int id;
        /**
         * Имя рейтинга.
         */
        private String name;
    }

    /**
     * Модель жанра.
     */
    @Data
    public static class Genre {
        /**
         * id жанра.
         */
        private int id;
        /**
         * Название жанра.
         */
        private String name;
    }
}
