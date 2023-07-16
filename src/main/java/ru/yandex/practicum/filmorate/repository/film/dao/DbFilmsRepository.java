package ru.yandex.practicum.filmorate.repository.film.dao;

import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Film.Genre;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository("DbFilmsRepository")
@RequiredArgsConstructor
public class DbFilmsRepository implements FilmRepository {
    /**
     * Объект шаблонного класса с базовым набором операций JDBC
     */
    private final NamedParameterJdbcOperations jdbcOperations;


    @Override
    public void addLike(int filmId, int userId) {
        if (!isUserLikeFilm(filmId, userId)) {
            String sqlQuery = "insert into LIKES(FILM_ID, WHO_LIKED_USER_ID) " +
                    "values (:filmId, :userId)";
            MapSqlParameterSource map = new MapSqlParameterSource();

            map.addValue("filmId", filmId);
            map.addValue("userId", userId);
            jdbcOperations.update(sqlQuery, map);
        }
    }


    @Override
    public void deleteLike(int filmId, int userId) {
        if (isUserLikeFilm(filmId, userId)) {
            String sqlQuery = "delete from LIKES " +
                    "where FILM_ID = :filmId and WHO_LIKED_USER_ID = :userId";
            MapSqlParameterSource map = new MapSqlParameterSource();

            map.addValue("filmId", filmId);
            map.addValue("userId", userId);
            jdbcOperations.update(sqlQuery, map);
        }
    }

    /**
     * Метод предназначен для проверки, существует ли уже лайк от пользователя фильму.
     *
     * @param filmId фильм, лайк к которому проверяется.
     * @param userId пользователь, лайк от которого проверяется.
     * @return true, если в БД существует запись о лайке пользователя фильма.
     */
    private boolean isUserLikeFilm(int filmId, int userId) {
        String sqlQuery = "select count(*) " +
                "from LIKES " +
                "where FILM_ID = :filmId and WHO_LIKED_USER_ID = :userId";
        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("filmId", filmId);
        map.addValue("userId", userId);
        return jdbcOperations.queryForObject(sqlQuery, map, Integer.class) == 1;
    }


    @Override
    public List<Film> getPopularsFilms(int count) {
        final String sqlQuery = "select * " +
                "from FILMS " +
                "left join (select FILM_ID, count(*) as likes_count from LIKES group by FILM_ID) as likes on likes.FILM_ID = FILMS.FILM_ID " +
                "left join MPA on FILMS.MPA_ID = MPA.MPA_ID " +
                "order by likes_count desc " +
                "limit :count";

        return jdbcOperations.query(sqlQuery, Map.of("count", count), new FilmRowMapper());
    }


    @Override
    public void create(Film film) {
        String sqlQuery = "insert into FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_ID) " +
                "values (:name, :description, :releaseDate, :duration, :rate, :mpaId)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcOperations.update(sqlQuery, getSqlParametersWhenCreateAndUpdate(film), keyHolder);
        film.setId(keyHolder.getKey().intValue());
        updateFilmGenres(film);
    }

    /**
     * Вспомогательный метод, удаляющий записи о жанрах фильма в БД.
     *
     * @param film фильм, информация о жанрах которого удалится.
     */
    private void deleteFilmGenres(Film film) {
        String sqlQuery = "delete from FILM_GENRES where FILM_ID = :filmId";
        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("filmId", film.getId());
        jdbcOperations.update(sqlQuery, map);
    }

    /**
     * Метод предназначен для обновления жанров фильма при каждом создании (происходит запись данных о жанрах фильма) и
     * каждом обновлении фильма (удаление старых записпей о жанрах и запись новых).
     *
     * @param film фильм, жанры которого сохраняются.
     */
    private void updateFilmGenres(Film film) {
        deleteFilmGenres(film);

        LinkedHashSet<Genre> genres = film.getGenres();

        if (genres != null) {
            final String sqlGenresQuery = "insert into FILM_GENRES(FILM_ID, GENRE_ID) values (:filmId, :genreId)";
            int i = 0;
            SqlParameterSource[] batchParams = new SqlParameterSource[genres.size()];

            for (Genre genre : genres) {
                MapSqlParameterSource params = new MapSqlParameterSource()
                        .addValue("genreId", genre.getId())
                        .addValue("filmId", film.getId());
                batchParams[i++] = params;
            }
            jdbcOperations.batchUpdate(sqlGenresQuery, batchParams);
        }
    }

    @Override
    public List<Genre> getTotalAvailableGenres() {
        final String sqlQuery = "select * from GENRES";
        List<Genre> totalGenres = jdbcOperations.query(sqlQuery, new GenresRowMapper());

        return totalGenres;
    }

    @Override
    public List<Film.MotionPictureAssociation> getTotalAvailableMpa() {
        final String sqlQuery = "select * from MPA";
        List<Film.MotionPictureAssociation> totalMpa = jdbcOperations.query(sqlQuery, new MpaRowMapper());
        return totalMpa;
    }


    @Override
    public Optional<Film.MotionPictureAssociation> getMpaById(int id) {
        String sqlQuery = "select * " +
                "from MPA " +
                "where MPA_ID = :id";
        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("id", id);

        List<Film.MotionPictureAssociation> mpa = jdbcOperations.query(sqlQuery, map, new MpaRowMapper());

        if (mpa.size() == 0) return Optional.empty();
        return Optional.of(mpa.get(0));
    }


    @Override
    public Optional<Genre> getGenreById(int id) {
        String sqlQuery = "select * " +
                "from GENRES " +
                "where GENRE_ID = :id";
        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("id", id);

        List<Genre> genres = jdbcOperations.query(sqlQuery, map, new GenresRowMapper());

        if (genres.size() == 0) return Optional.empty();
        return Optional.of(genres.get(0));
    }


    @Override
    public List<Genre> getFilmGenres(Film film) {
        final String sqlQuery = "select * from FILM_GENRES " +
                "where FILM_ID = :filmId";
        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("filmId", film.getId());

        List<Genre> filmGenres = jdbcOperations.query(sqlQuery, map, new GenresRowMapper());

        return filmGenres;
    }


    @Override
    public void update(Film film) {
        String sqlQuery = "update FILMS " +
                "set FILM_ID = :filmId, NAME = :name, DESCRIPTION = :description, RELEASE_DATE = :releaseDate," +
                "DURATION = :duration, RATE = :rate, MPA_ID = :mpaId " +
                "where FILM_ID = :filmId";

        jdbcOperations.update(sqlQuery, getSqlParametersWhenCreateAndUpdate(film));
        updateFilmGenres(film);
    }

    /**
     * Вспомогательный метод, предназначенный для формирования параметров именнованного sql-запроса.
     *
     * @param film фильм, параметры которого составляют запрос.
     * @return таблица параметров запроса для jdbc
     */
    private MapSqlParameterSource getSqlParametersWhenCreateAndUpdate(Film film) {
        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("name", film.getName());
        map.addValue("description", film.getDescription());
        map.addValue("releaseDate", film.getReleaseDate());
        map.addValue("duration", film.getDuration());
        map.addValue("rate", film.getRate());
        map.addValue("mpaId", film.getMpa().getId());
        map.addValue("filmId", film.getId());
        return map;
    }


    @Override
    public void delete(Film film) {
        throw new NotYetImplementedException("Удаление фильмов не реализовано");
    }

    /**
     * Вспомогательный метод, осуществляюсь запрос к БД и установку жанров фильму.
     *
     * @param film фильм, жанры которому будут установлены.
     */
    private void setFilmGenresTo(Film film) {
        final String sqlQueryForGenres = "select * " +
                "from FILM_GENRES " +
                "left join GENRES G2 on G2.GENRE_ID = FILM_GENRES.GENRE_ID " +
                "where FILM_ID = :filmId";
        SqlRowSet sqlRowSet = jdbcOperations.queryForRowSet(sqlQueryForGenres, Map.of("filmId", film.getId()));

        while (sqlRowSet.next()) {
            int genreId = sqlRowSet.getInt("GENRE_ID");
            String genreName = sqlRowSet.getString("GENRE_NAME");
            Genre genre = new Genre();
            genre.setId(genreId);
            genre.setName(genreName);
            film.getGenres().add(genre);
        }
    }

    @Override
    public Optional<Film> get(Integer id) {
        final String sqlQuery = "select * from FILMS " +
                "left join MPA M on FILMS.MPA_ID = M.MPA_ID " +
                "where FILM_ID = :id ";
        List<Film> films = jdbcOperations.query(sqlQuery, Map.of("id", id), new FilmRowMapper());

        if (films.size() == 0) return Optional.empty();

        Film film = films.get(0);

        setFilmGenresTo(film);
        return Optional.of(film);
    }


    @Override
    public List<Film> getAll() {
        final String sqlQuery = "select * " +
                "from FILMS " +
                "left join MPA M on FILMS.MPA_ID = M.MPA_ID";
        /*
         * Хеш-мапа для всех фильмов вида:
         * Ключ - id фильма.
         * Значение - фильм.
         */
        Map<Integer, Film> films = jdbcOperations.query(sqlQuery, new FilmRowMapper()).stream()
                .collect(Collectors.toMap(film -> film.getId(), film -> film));

        final String sqlQueryForGenres = "select * " +
                "from FILM_GENRES " +
                "left join GENRES G2 on G2.GENRE_ID = FILM_GENRES.GENRE_ID";
        /*
         * Результат запроса:
         * FILM_ID - id фильма.
         * GENRE_ID - id жанра.
         * GENRE_NAME - имя жанра.
         */
        SqlRowSet sqlRowSet = jdbcOperations.queryForRowSet(sqlQueryForGenres, Map.of());

        /*
         * Для каждого фильма из полученной хеш-мапы устанавливается жанр.
         * id и название жанра, а также id фильма, которому они соответствуют известны из второго запроса.
         * Объект фильма находится по его id из хеш-мапы, создается объект жанра и устанавливается ему в поле.
         */
        while (sqlRowSet.next()) {
            int filmId = sqlRowSet.getInt("FILM_ID");
            int genreId = sqlRowSet.getInt("GENRE_ID");
            String genreName = sqlRowSet.getString("GENRE_NAME");
            Genre genre = new Genre();

            genre.setId(genreId);
            genre.setName(genreName);
            films.get(filmId).getGenres().add(genre);
        }
        return List.copyOf(films.values());
    }


    @Override
    public void deleteAll() {
        throw new NotYetImplementedException("Удаление всех фильмов не реализовано");
    }


    @Override
    public List<Integer> getAllIds() {
        throw new NotYetImplementedException("Получение всех ids не реализовано");
    }

    private static class FilmRowMapper implements RowMapper<Film> {
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            Film film = new Film();

            film.setId(rs.getInt("FILM_ID"));
            film.setName(rs.getString("NAME"));
            film.setDescription(rs.getString("DESCRIPTION"));
            film.setRate(rs.getInt("RATE"));
            film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
            film.setDuration(rs.getInt("DURATION"));

            Film.MotionPictureAssociation mpa = new Film.MotionPictureAssociation();

            mpa.setId(rs.getInt("MPA_ID"));
            mpa.setName(rs.getString("MPA"));
            film.setMpa(mpa);
            film.setGenres(new LinkedHashSet<>());
            return film;
        }
    }

    private static class GenresRowMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            Genre genre = new Genre();

            genre.setId(rs.getInt("GENRE_ID"));
            genre.setName(rs.getString("GENRE_NAME"));
            return genre;
        }
    }

    private static class MpaRowMapper implements RowMapper<Film.MotionPictureAssociation> {
        @Override
        public Film.MotionPictureAssociation mapRow(ResultSet rs, int rowNum) throws SQLException {
            Film.MotionPictureAssociation mpa = new Film.MotionPictureAssociation();

            mpa.setId(rs.getInt("MPA_ID"));
            mpa.setName(rs.getString("MPA"));
            return mpa;
        }
    }
}
