-- liquibase formatted sql
-- changeset Homychok:1

CREATE TABLE avatar(
                                     id              SERIAL PRIMARY KEY,
                                     media_type      VARCHAR,
                                     file_size       BIGINT,
                                     data            BYTEA
);