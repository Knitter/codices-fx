DROP TABLE IF EXISTS item_genre;
DROP TABLE IF EXISTS item_author;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS publisher;
DROP TABLE IF EXISTS collection;
DROP TABLE IF EXISTS series;
DROP TABLE IF EXISTS author;
DROP TABLE IF EXISTS genre;
DROP TABLE IF EXISTS format;
DROP TABLE IF EXISTS account;

CREATE TABLE account
(
    id        INTEGER PRIMARY KEY,
    username  TEXT    NOT NULL UNIQUE,
    email     TEXT    NOT NULL,
    name      TEXT    NOT NULL,
    active    INTEGER NOT NULL,
    password  TEXT    NOT NULL,
    createdAt INTEGER NOT NULL,
    updatedAt INTEGER NOT NULL,
    authKey   TEXT
);

CREATE TABLE publisher
(
    id        INTEGER PRIMARY KEY,
    name      TEXT    NOT NULL,
    ownedById INTEGER NOT NULL,
    summary   TEXT,
    website   TEXT,
    FOREIGN KEY (ownedById) REFERENCES account (id)
);

CREATE TABLE series
(
    id         INTEGER PRIMARY KEY,
    name       TEXT    NOT NULL,
    ownedById  INTEGER NOT NULL,
    completed  INTEGER NOT NULL,
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
    description TEXT,
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

CREATE TABLE genre
(
    id        INTEGER PRIMARY KEY,
    name      TEXT    NOT NULL,
    ownedById INTEGER NOT NULL,
    FOREIGN KEY (ownedById) REFERENCES account (id)
);

CREATE TABLE format
(
    type      TEXT    NOT NULL,
    name      TEXT    NOT NULL,
    ownedById INTEGER NOT NULL,
    PRIMARY KEY (type, name, ownedById),
    FOREIGN KEY (ownedById) REFERENCES account (id)
);

-- A paper book, ebook or audio book managed by Codiges-fx
CREATE TABLE item
(
    id            INTEGER PRIMARY KEY,
    title         TEXT    NOT NULL,
    ownedById     INTEGER NOT NULL,
    type          TEXT    NOT NULL, -- "ebook", "audio", "paper"
    translated    INTEGER NOT NULL,
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
    volume        TEXT,
    rating        INTEGER,
    url           TEXT,
    review        TEXT,
    cover         TEXT,
    filename      TEXT,
    fileLocation  TEXT,
    narrator      TEXT,
    bitrate       TEXT,
    boughtFrom    TEXT,
    duration      INTEGER,          -- in minutes
    sizeBytes     INTEGER,          -- in KB
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

CREATE TABLE item_author
(
    itemId   INTEGER NOT NULL,
    authorId INTEGER NOT NULL,
    PRIMARY KEY (itemId, authorId),
    FOREIGN KEY (itemId) REFERENCES item (id),
    FOREIGN KEY (authorId) REFERENCES author (id)
);

CREATE TABLE item_genre
(
    itemId  INTEGER NOT NULL,
    genreId INTEGER NOT NULL,
    PRIMARY KEY (itemId, genreId),
    FOREIGN KEY (itemId) REFERENCES item (id),
    FOREIGN KEY (genreId) REFERENCES genre (id)
);