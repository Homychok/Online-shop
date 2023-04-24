-- liquibase formatted sql
-- changeset Homychok:1

CREATE TABLE users(
                       id              SERIAL PRIMARY KEY,
                       username        VARCHAR UNIQUE NOT NULL,
                       first_name      VARCHAR NOT NULL,
                       last_name       VARCHAR NOT NULL,
                       phone           VARCHAR NOT NULL,
                       password        VARCHAR NOT NULL,
                       enabled         BOOLEAN,
                       role            VARCHAR,
                       avatar_id       INTEGER REFERENCES avatar(id)
);