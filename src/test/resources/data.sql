/*
 Заполнение БД
 */

delete
from FILM_GENRES;

delete
from LIKES;

delete
from FILMS;

delete
from MPA;

delete
from GENRES;

delete
from FRIENDS_REQUESTS;

delete
from USERS;

alter table FILMS
    alter column FILM_ID
        restart with 1;

alter table USERS
    alter column USER_ID
        restart with 1;

alter table MPA
    alter column MPA_ID
        restart with 1;

alter table GENRES
    alter column GENRE_ID
        restart with 1;

insert into MPA(MPA)
values ('G');

insert into MPA(MPA)
values ('PG');

insert into MPA(MPA)
values ('PG-13');

insert into MPA(MPA)
values ('R');

insert into MPA(MPA)
values ('NC-17');

insert into GENRES(GENRE_NAME)
values ('Комедия');

insert into GENRES(GENRE_NAME)
values ('Драма');

insert into GENRES(GENRE_NAME)
values ('Мультфильм');

insert into GENRES(GENRE_NAME)
values ('Триллер');

insert into GENRES(GENRE_NAME)
values ('Документальный');

insert into GENRES(GENRE_NAME)
values ('Боевик');

/*
 Добавление тестового пользователя
 */

/*
 Добавление тестового пользователя
 */

insert into USERS (EMAIL, LOGIN, NAME, BIRTHDAY)
values ('test@test.ru', 'test login', 'test name', '2000-08-17');

/*
 Добавление тестового фильма
 */

insert into FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_ID)
values ('test film name', 'test film description', '2012-11-15', 90, 2, 2);