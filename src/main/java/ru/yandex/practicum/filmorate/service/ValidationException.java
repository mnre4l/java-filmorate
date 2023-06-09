package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;


/**
 * Исключение, бросаемое при валидации.
 * Я не очень уверен, что оно нужно (просят в тз), потому что @Valid
 * бросает собственное.
 * Используется для записи в лог с соответствующим сообщением
 */
@Slf4j
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
        log.info(message);
    }
}
