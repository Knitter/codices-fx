-- DROP TABLE item;
-- DROP TABLE publisher;
-- DROP TABLE collection;
-- DROP TABLE series;
-- DROP TABLE account;
-- DROP TABLE author ;

CREATE TABLE account
(
    id         INTEGER PRIMARY KEY,
    username   TEXT    NOT NULL UNIQUE,
    email      TEXT    NOT NULL,
    name       TEXT    NOT NULL,
    active     INTEGER NOT NULL,
    password   TEXT    NOT NULL,
    createdAt INTEGER NOT NULL,
    updatedAt INTEGER NOT NULL,
    resetToken TEXT    NULL UNIQUE,
    authKey    TEXT
);

CREATE TABLE publisher
(
    id        INTEGER PRIMARY KEY,
    name      TEXT    NOT NULL,
    ownedById INTEGER NOT NULL,
    summary   TEXT,
    website   TEXT,
    logo      TEXT,
    FOREIGN KEY (ownedById) REFERENCES account (id)
);

CREATE TABLE series
(
    id         INTEGER PRIMARY KEY,
    name       TEXT    NOT NULL,
    ownedById  INTEGER NOT NULL,
    completed   INTEGER NOT NULL,
    bookCount  INTEGER,
    ownedCount INTEGER,
    FOREIGN KEY (ownedById) REFERENCES account (id)
);

-- A collection of books either created by the user or by a publisher/seller (e.g. HumbleBundle)
CREATE TABLE collection
(
    id          INTEGER PRIMARY KEY,
    name        TEXT    NOT NULL,
    ownedById   INTEGER NOT NULL,
    publishDate TEXT,
    publishYear INTEGER,
    FOREIGN KEY (ownedById) REFERENCES account (id)
);

CREATE TABLE author
(
    id        INTEGER PRIMARY KEY,
    name      TEXT    NOT NULL,
    ownedById INTEGER NOT NULL,
    surname   TEXT,
    biography TEXT,
    website   TEXT,
    photo     TEXT,
    FOREIGN KEY (ownedById) REFERENCES account (id)
);

-- A paper book, ebook or audio book
CREATE TABLE item
(
    id            INTEGER PRIMARY KEY,
    title         TEXT    NOT NULL,
    ownedById     INTEGER NOT NULL,
    type          TEXT    NOT NULL, -- ebook, audio, paper
    translated    INTEGER NOT NULL,
    favorite      INTEGER NOT NULL,
    read          INTEGER NOT NULL,
    copies        INTEGER NOT NULL,
    subtitle      TEXT,
    originalTitle TEXT,
    plot          TEXT,
    isbn          TEXT,
    format        TEXT,
    pageCount     INTEGER,
    publishDate   TEXT,
    publishYear   INTEGER,
    addedOn       TEXT,
    language      TEXT,
    edition       TEXT,
    rating        REAL,
    ownRating     REAL,
    url           TEXT,
    review        TEXT,
    cover         TEXT,
    filename      TEXT,
    narrator      TEXT,
    boughtFrom    TEXT,
    duration      INTEGER,          -- minutes
    orderInSeries INTEGER,
    publisherId   INTEGER,
    seriesId      INTEGER,
    collectionId  INTEGER,
    duplicatesId  INTEGER,
    FOREIGN KEY (ownedById) REFERENCES account (id),
    FOREIGN KEY (publisherId) REFERENCES publisher (id),
    FOREIGN KEY (seriesId) REFERENCES series (id),
    FOREIGN KEY (collectionId) REFERENCES collection (id),
    FOREIGN KEY (duplicatesId) REFERENCES item (id)
);
