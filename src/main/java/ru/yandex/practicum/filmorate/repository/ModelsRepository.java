package ru.yandex.practicum.filmorate.repository;


import java.util.List;

/**
 * Интерфейс, описывающий CRUD операции репозитория.
 * @param <T> тип объекта, который хранится в репозитории.
 */
public interface ModelsRepository<T> {
    void create(T t);

    void update(T t);

    void delete(T t);

    /**
     * Предполагается, что получение сущности происходит по id
     * @param id идентификатор хранимого объекта
     * @return хранимый объект
     */
    T get(int id);
    List<T> getAll();
}
