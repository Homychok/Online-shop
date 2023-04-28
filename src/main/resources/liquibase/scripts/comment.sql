-- liquibase formatted sql
-- changeset Homychok:1
CREATE TABLE comment(
                         pk              SERIAL PRIMARY KEY NOT NULL,
                         created_at      TIMESTAMP NOT NULL,
                         text            TEXT NOT NULL,
                         ads_pk          INTEGER REFERENCES ads(id),
                         author_pk       INTEGER REFERENCES users(id)
);