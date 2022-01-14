DROP TABLE IF EXISTS connection;
DROP SEQUENCE IF EXISTS connection_id_seq;
DROP TABLE IF EXISTS client;
DROP SEQUENCE IF EXISTS client_id_seq;

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
    FOREIGN KEY (client_id) references Client (id)
);

