DROP TABLE IF EXISTS Client;

CREATE TABLE Client
(
    id         serial primary key,
    created_at timestamp default now(),
    updated_at timestamp default now(),
    username   varchar(30) not null,
    password   varchar(255) not null
)