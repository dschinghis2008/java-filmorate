insert into USERS (NAME, LOGIN, EMAIL, BIRTHDAY)
values ('user1','user1','u1@home','2000-01-01');
insert into USERS (NAME, LOGIN, EMAIL, BIRTHDAY)
values ('user2','user2','u2@home','2000-01-01');
insert into USERS (NAME, LOGIN, EMAIL, BIRTHDAY)
values ('user3','user3','u3@home','2000-01-01');
insert into FRIENDS (ID_USER, ID_FRIEND, STATUS)
values (1,2,1);
insert into FRIENDS (ID_USER, ID_FRIEND, STATUS)
values (2,3,0);
insert into MPA_RATING (NAME)
values ('G');
insert into MPA_RATING (NAME)
values ('PG');
insert into MPA_RATING (NAME)
values ('PG-13');
insert into MPA_RATING (NAME)
values ('R');
insert into MPA_RATING (NAME)
values ('NC-17');
insert into GENRES (NAME)
values ('Комедия');
insert into GENRES (NAME)
values ('Драма');
insert into GENRES (NAME)
values ('Мультфильм');
insert into GENRES (NAME)
values ('Триллер');
insert into GENRES (NAME)
values ('Документальный');
insert into GENRES (NAME)
values ('Боевик');
insert into FILMS (NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE, MPA)
values ('film1','desc film2','1990-01-01',60,0,1);
insert into FILMS (NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE, MPA)
values ('film2','desc film2','2000-01-01',60,0,1);
insert into FILM_GENRE_LINK (ID_GENRE, ID_FILM)
values (1,1);
insert into FILM_GENRE_LINK (ID_GENRE, ID_FILM)
values (3,2);
insert into RATING (id_user,id_film)
values (1,1)