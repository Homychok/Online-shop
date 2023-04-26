-- liquibase formatted sql
-- changeset Homychok:1

CREATE TABLE ads(
                     id              SERIAL PRIMARY KEY,
                     title           VARCHAR NOT NULL,
                     description     TEXT,
                     price           INTEGER NOT NULL,
                     author_id       INT REFERENCES users(id),
                     image           BYTEA
--                      image_id        INTEGER REFERENCES image(id)
);