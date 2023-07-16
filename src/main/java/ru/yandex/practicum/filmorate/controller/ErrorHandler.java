package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.FriendsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

/**
 * Класс предназначен для обработки исключений.
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    /**
     * Обработка исключений, связанных с отсутствием запрашиваемых сущностей.
     *
     * @param e - исключение типа NotFoundException, выбрасываемое при отсутствии запрашиваемой сущности.
     * @return объект ответа, содержащий сообщение об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.info("Error: " + e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Обработка исключений, связанных с логикой друзей.
     *
     * @param e исключение типа FriendsException, выбрасываемое при недопустимых действиях по логике друзей (добавление
     *          в друзья уже добавленного в друзья пользователя и т.д.)
     * @return объект ответа, содержащий сообщение об ошибке.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUsersAreNotFriendsException(final FriendsException e) {
        log.info("Error: " + e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Обработка прочих непроверямых исключений.
     *
     * @param e непроверяемое исключение.
     * @return объект ответа, содержащий сообщение об ошибке.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final RuntimeException e) {
        log.warn("Error: " + e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

}
