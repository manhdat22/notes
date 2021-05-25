CREATE DATABASE project_notes;

USE project_notes;

CREATE TABLE users (
    userid INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255),
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    gender VARCHAR(255)
);

CREATE TABLE notes (
    noteid INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    userid INT,
    datecreated TIMESTAMP,
    title VARCHAR(255),
    content TEXT
);
