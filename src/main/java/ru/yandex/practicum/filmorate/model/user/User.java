package ru.yandex.practicum.filmorate.model.user;

import lombok.Data;
import ru.yandex.practicum.filmorate.service.validation.Marker;

import javax.validation.constraints.*;
import java.time.LocalDate;


/**
 * Класс описывает пользователя, который регистрируется в системе.
 */
@Data
public class User {
    /**
     * Идентификатор пользователя.
     */
    @Digits(integer = Integer.MAX_VALUE, fraction = 0, groups = Marker.OnUpdate.class)
    @Positive(groups = Marker.OnUpdate.class)
    @Null(groups = Marker.OnCreate.class)
    @NotNull(groups = Marker.OnUpdate.class)
    private Integer id;
    /**
     * Email пользователя.
     */
    @Email
    @NotNull
    private String email;
    /**
     * Логин.
     */
    @NotBlank
    private String login;
    /**
     * Имя.
     */
    private String name;
    /**
     * День рождения пользователя.
     */
    @Past
    private LocalDate birthday;
}
