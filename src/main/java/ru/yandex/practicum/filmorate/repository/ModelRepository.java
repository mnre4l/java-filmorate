package ru.yandex.practicum.filmorate.repository;

import jdk.jshell.spi.ExecutionControl;

import java.util.Collection;


/**
 * Интерфейс, описывающий CRUD операции репозитория.
 *
 * @param <T> тип объекта, который хранится в репозитории.
 */
public interface ModelRepository<T> {
    void create(T t);

    void update(T t);

    void delete(T t) throws ExecutionControl.NotImplementedException;

    /**
     * Предполагается, что получение сущности происходит по id
     *
     * @param id идентификатор хранимого объекта
     * @return хранимый объект
     */
    T get(Integer id);

    Collection<T> getAll();

    void deleteAll();

    Collection<Integer> getAllId();
}
