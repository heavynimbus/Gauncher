DROP TABLE IF EXISTS connection;
DROP SEQUENCE IF EXISTS connection_id_seq;
DROP TABLE IF EXISTS client;
DROP SEQUENCE IF EXISTS client_id_seq;
DROP TABLE IF EXISTS game;
DROP SEQUENCE IF EXISTS game_id_seq;
DROP TABLE IF EXISTS players_games;
DROP SEQUENCE IF EXISTS players_games_id_seq;

CREATE TABLE client
(
    id         serial primary key,
    created_at timestamp default now(),
    updated_at timestamp default now(),
    username   varchar(30) unique not null,
    password   varchar(255)       not null
);

CREATE TABLE connection
(
    id          serial primary key,
    created_at  timestamp default now(),
    updated_at  timestamp default now(),
    client_id   integer not null,
    socket_ip   varchar(50),
    socket_port integer,
    status      varchar(10),
    FOREIGN KEY (client_id) REFERENCES client (id)
);

--CREATE TABLE game
--(
--    id         serial primary key,
--    created_at timestamp default now(),
--    updated_at timestamp default now(),
--    name       varchar(20)
--);

--INSERT INTO game(name)
--VALUES ('chat'),
--       ('tictactoe');

--CREATE TABLE players_games
--(
--    client_id integer not null,
--    game_id   integer not null,
--    FOREIGN KEY (client_id) REFERENCES client (id),
--    FOREIGN KEY (game_id) REFERENCES game (id)
--);

--CREATE TABLE chat_messages
--(
--    id         serial primary key,
--    created_at timestamp default now(),
--    updated_at timestamp default now(),
--    client_id  integer       not null,
--    message    varchar(1024) not null,
--    FOREIGN KEY (client_id) REFERENCES client (id)
--);