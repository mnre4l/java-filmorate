package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс предназначен для формирования ответа в случае перехвата исключения.
 * Объект этого класса помещается в тело ответа в случае перехвата исключения.
 */
@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
    private String message;

    /**
     * @param message сообщение об исключении.
     */
    public ErrorResponse(String message) {
        this.message = message;
    }
}
