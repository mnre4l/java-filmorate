package ru.yandex.practicum.filmorate.exception;

/**
 * Исключение, связанное с ошибкой при добавлении в друзья/удалении из друзей и т.д. пользователей.
 */
public class FriendsException extends RuntimeException {
    /**
     * @param message сообщение, содержащие информацию об ошибке.
     */
    public FriendsException(String message) {
        super(message);
    }
}
