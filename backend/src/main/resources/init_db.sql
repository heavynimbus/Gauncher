DROP TABLE IF EXISTS Client;

CREATE TABLE Client
(
    id         serial primary key,
    username   varchar(30) unique,
    password   varchar(255),
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

INSERT INTO Client(username, password)
VALUES ('user', 'pass');