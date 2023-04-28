-- liquibase formatted sql
-- changeset Homychok:1
CREATE TABLE comment(
                         pk              SERIAL PRIMARY KEY NOT NULL,
                         created_at      TIMESTAMP NOT NULL,
                         text            TEXT NOT NULL,
                         ads_pk          INTEGER REFERENCES ads(pk),
                         author_id       INTEGER REFERENCES users(id)
);