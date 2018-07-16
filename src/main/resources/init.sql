/*
    Database initialization script that runs on every web-application redeployment.
*/


DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS columns;
DROP TABLE IF EXISTS schedules;
DROP TABLE IF EXISTS users;


CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email TEXT UNIQUE NOT NULL,
    password TEXT,
    username TEXT NOT NULL,
    userrole TEXT NOT NULL
);

CREATE TABLE schedules (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    userid INTEGER NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(id)
);

CREATE TABLE columns (
        id SERIAL PRIMARY KEY,
        title TEXT NOT NULL,
        schid INTEGER NOT NULL,
        FOREIGN KEY (schid) REFERENCES schedules(id)
);

CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    userid INTEGER NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(id)
);

CREATE TABLE events (
    id SERIAL PRIMARY KEY,
    columnid INTEGER NOT NULL,
    taskid INTEGER NOT NULL,
    FOREIGN KEY (columnid) REFERENCES columns(id),
    FOREIGN KEY (taskid) REFERENCES tasks(id),
    starttime INTEGER NOT NULL,
    endtime INTEGER NOT NULL,
    description TEXT
);

INSERT INTO users (email, password, username, userrole) VALUES
	('a', 'a', 'a', 'ADMIN'),
	('b', 'b', 'Bob', 'USER'),
	('commonhobo@gmail.com', 'c', 'Carlos', 'USER'),
	('snake@gmail.com', 's', 'Sam', 'USER'),
	('fatrat@gmail.com', 'f', 'Rob', 'USER'),
	('boomer@gmail.com', 'bb', 'Mary', 'USER'),
	('hanSolo@yahoo.com', 'h', 'Solo', 'USER'),
	('somechineseman@gmail.com', 'l', 'Lee', 'USER'),
	('marysue@gmail.com', 'm', 'MarySue', 'USER'),
	('pigpie@gmail.com', 'p', 'Peggy', 'USER'),
	('redneck@nowheretown.com', 'r', 'X', 'USER');

INSERT INTO schedules (name, userid) VALUES
    ('Friday of Test Bela', '2'),
    ('Sunday - Build a fusion rector in the garage', '2'),
    ('Monday - Tuesday of Test Bela', '2');

INSERT INTO columns (title, schid) VALUES
    ('Friday', 1),
    ('Idontwannawork', 2),
    ('Almostweekend', 2);

INSERT INTO tasks (title, userid) VALUES
    ('Party time', 2),
    ('Sleep', 2),
    ('Work', 2),
    ('Meaning of Life', 2),
    ('Not wanting to work', 2);

INSERT INTO events (columnid, taskid, starttime, endtime, description) VALUES
    (1, 1, 18, 24, 'party hard'),
    (2, 2, 0, 5, 'late weekend'),
    (2, 3, 7, 9, 'depressing'),
    (2, 4, 9, 14, 'more depressing'),
    (3, 5, 5, 15, 'productivity spike');