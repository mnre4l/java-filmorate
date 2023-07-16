create table if not exists GENRES
(
    GENRE_ID   IDENTITY          NOT NULL PRIMARY KEY,
    GENRE_NAME CHARACTER VARYING not null,
    constraint "GENRES_pk"
        primary key (GENRE_ID)
);

create table if not exists MPA
(
    MPA_ID IDENTITY          NOT NULL PRIMARY KEY,
    MPA    CHARACTER VARYING not null,
    constraint "MPA_pk"
        primary key (MPA_ID)
);

create table if not exists FILMS
(
    FILM_ID      IDENTITY          NOT NULL PRIMARY KEY,
    NAME         CHARACTER VARYING not null,
    DESCRIPTION  CHARACTER VARYING not null,
    RELEASE_DATE TIMESTAMP         not null,
    DURATION     INTEGER           not null,
    RATE         INTEGER,
    MPA_ID       INTEGER,
    constraint FILM_ID
        primary key (FILM_ID),
    constraint "FILMS_MPA_MPA_ID_fk"
        foreign key (MPA_ID) references MPA
);

create table if not exists FILM_GENRES
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint "FILM_GENRES_FILMS_FILM_ID_fk"
        foreign key (FILM_ID) references FILMS,
    constraint "FILM_GENRES_GENRES_GENRE_ID_fk"
        foreign key (GENRE_ID) references GENRES
);

create table if not exists USERS
(
    USER_ID  IDENTITY NOT NULL PRIMARY KEY,
    EMAIL    CHARACTER VARYING,
    LOGIN    CHARACTER VARYING,
    NAME     CHARACTER VARYING,
    BIRTHDAY TIMESTAMP,
    constraint "USERS_pk"
        primary key (USER_ID)
);

create table if not exists FRIENDS_REQUESTS
(
    USER_ID_FROM INTEGER not null,
    USER_ID_TO   INTEGER not null,
    constraint "FRIENDS_REQUESTS_USERS_USER_ID_fk"
        foreign key (USER_ID_TO) references USERS,
    constraint "FRIENDS_REQUESTS_USERS_USER_ID_fk2"
        foreign key (USER_ID_FROM) references USERS
);

create table if not exists LIKES
(
    FILM_ID           INTEGER not null,
    WHO_LIKED_USER_ID INTEGER not null,
    constraint "LIKES_FILMS_FILM_ID_fk"
        foreign key (FILM_ID) references FILMS,
    constraint "LIKES_USERS_USER_ID_fk"
        foreign key (WHO_LIKED_USER_ID) references USERS
);