-- liquibase formatted sql
-- changeset stanislav:1
CREATE TABLE socks (
  id            SERIAL PRIMARY KEY,
  color         VARCHAR(30) NOT NULL,
  cotton_part   INTEGER
);

CREATE TABLE stock (
  id        SERIAL PRIMARY KEY,
  quantity  INTEGER,
  socks_id  INTEGER REFERENCES socks(id)
);