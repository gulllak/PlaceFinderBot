--liquibase formatted sql
--changeset evgenii:202403241523.init_schema.sql

CREATE TABLE IF NOT EXISTS users (
    id bigint PRIMARY KEY NOT NULL,
    condition varchar NOT NULL,
    filter_state varchar
);

CREATE TABLE IF NOT EXISTS filters (
    user_id bigint PRIMARY KEY,
    place_type VARCHAR,
    distance INTEGER,
    rating float4,
    review_count INTEGER,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

