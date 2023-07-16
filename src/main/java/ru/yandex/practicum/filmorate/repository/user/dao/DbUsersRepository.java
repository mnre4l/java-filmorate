package ru.yandex.practicum.filmorate.repository.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Класс представляет собой DAO-класс для сущностей пользователей, хранимых в БД.
 */
@Repository("DbUsersRepository")
@RequiredArgsConstructor
public class DbUsersRepository implements UserRepository {
    /**
     * Объект шаблонного класса с базовым набором операций JDBC
     */
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public void create(User user) {
        String sqlQuery = "insert into USERS (EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "values (:email, :login, :name, :birthday)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("email", user.getEmail());
        map.addValue("login", user.getLogin());
        map.addValue("name", user.getName());
        map.addValue("birthday", user.getBirthday());
        jdbcOperations.update(sqlQuery, map, keyHolder);
        user.setId(keyHolder.getKey().intValue());
    }

    @Override
    public void update(User user) {
        String sqlQuery = "update USERS " +
                "set EMAIL = :email, LOGIN = :login, NAME = :name, BIRTHDAY = :birthday " +
                "where USER_ID = :id";
        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("email", user.getEmail());
        map.addValue("login", user.getLogin());
        map.addValue("name", user.getName());
        map.addValue("birthday", user.getBirthday());
        map.addValue("id", user.getId());
        jdbcOperations.update(sqlQuery, map);
    }

    @Override
    public void delete(User user) {
        final String sqlQuery = "delete from USERS where USER_ID = :id";

        jdbcOperations.update(sqlQuery, Map.of("id", user.getId()));
    }

    @Override
    public Optional<User> get(Integer id) {
        final String sqlQuery = "select USER_ID, NAME, EMAIL, LOGIN, BIRTHDAY " +
                "from USERS " +
                "where USER_ID = :id";
        List<User> users = jdbcOperations.query(sqlQuery, Map.of("id", id), new UserRowMapper());

        return users.size() == 0 ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public List<User> getAll() {
        final String sqlQuery = "select USER_ID, NAME, EMAIL, LOGIN, BIRTHDAY " +
                "from USERS";

        return jdbcOperations.query(sqlQuery, new UserRowMapper());
    }

    @Override
    public void deleteAll() {
        final String sqlQuery = "delete from USERS";

        jdbcOperations.update(sqlQuery, Map.of());
    }

    @Override
    public List<Integer> getAllIds() {
        return getAll().stream()
                .map(user -> user.getId())
                .collect(Collectors.toList());
    }

    @Override
    public void addToFriends(Integer id, Integer friendId) {
        final String sqlQuery = "insert into FRIENDS_REQUESTS (USER_ID_FROM, USER_ID_TO) " +
                "values (:id, :friendId)";

        jdbcOperations.update(sqlQuery, Map.of("id", id, "friendId", friendId));
    }

    @Override
    public void deleteFromFriends(Integer id, Integer friendId) {
        final String sqlQuery = "delete from FRIENDS_REQUESTS " +
                "where USER_ID_FROM = :id and USER_ID_TO = :friendId";

        jdbcOperations.update(sqlQuery, Map.of("id", id, "friendId", friendId));
    }

    @Override
    public List<User> getFriends(Integer id) {
        final String sqlQuery = "select USERS.USER_ID, USERS.NAME, USERS.EMAIL, USERS.LOGIN, USERS.BIRTHDAY " +
                "from FRIENDS_REQUESTS " +
                "left join USERS on USERS.USER_ID = FRIENDS_REQUESTS.USER_ID_TO " +
                "where :id = FRIENDS_REQUESTS.USER_ID_FROM";

        return jdbcOperations.query(sqlQuery, Map.of("id", id), new UserRowMapper());
    }

    @Override
    public List<User> getConfirmFriends(Integer id) {
        final String sqlQuery = "select USERS.USER_ID, USERS.NAME, USERS.EMAIL, USERS.LOGIN, USERS.BIRTHDAY " +
                "from FRIENDS_REQUESTS " +
                "left join USERS on USERS.USER_ID = FRIENDS_REQUESTS.USER_ID_FROM " +
                "where USER_ID in (select USERS.USER_ID " +
                "from FRIENDS_REQUESTS " +
                "left join USERS on USERS.USER_ID = FRIENDS_REQUESTS.USER_ID_TO " +
                "where :id = FRIENDS_REQUESTS.USER_ID_FROM)";

        return jdbcOperations.query(sqlQuery, Map.of("id", id), new UserRowMapper());
    }


    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();

            user.setId(rs.getInt("USER_ID"));
            user.setName(rs.getString("NAME"));
            user.setEmail(rs.getString("EMAIL"));
            user.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());
            user.setLogin(rs.getString("LOGIN"));
            return user;
        }
    }
}
