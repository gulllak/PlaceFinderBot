--liquibase formatted sql
--changeset evgenii:202403241523.init_schema.sql

CREATE TABLE IF NOT EXISTS users (
    id bigint PRIMARY KEY NOT NULL,
    condition varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS filters (
    user_id bigint PRIMARY KEY,
    place_type VARCHAR,
    distance INTEGER,
    raiting float4,
    reviewCount INTEGER,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

