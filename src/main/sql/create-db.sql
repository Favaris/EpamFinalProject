DROP DATABASE IF EXISTS time_accounting_db;
CREATE DATABASE time_accounting_db;
USE time_accounting_db;

/* -- not in use, left it here for better times...--
CREATE USER 'userlogin'@'localhost' IDENTIFIED BY 'userpass';
GRANT ALL PRIVILEGES ON time_accounting_db.* TO 'userlogin'@'localhost';
*/

-- option where roles are stored in an external table --
/*
DROP TABLE IF EXISTS roles;

CREATE TABLE IF NOT EXISTS roles (
	id TINYINT PRIMARY KEY,
    name VARCHAR(16) NOT NULL UNIQUE
);

INSERT INTO roles VALUES (1, "user"), (2, "admin");

DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
	id INT PRIMARY KEY AUTO_INCREMENT,
    login VARCHAR(16) NOT NULL UNIQUE,
    password VARCHAR(32) NOT NULL,
    name VARCHAR(30) NOT NULL,
    surname VARCHAR(30) NOT NULL,
    role_id TINYINT NULL DEFAULT 1,
    CONSTRAINT fk_user_role_id 
		FOREIGN KEY (role_id)
        REFERENCES roles(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);
*/

DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
	id INT PRIMARY KEY AUTO_INCREMENT,
    login VARCHAR(16) NOT NULL UNIQUE,
    password VARCHAR(32) NOT NULL,
    name VARCHAR(30) NOT NULL,
    surname VARCHAR(30) NOT NULL,
    role ENUM("user", "admin") DEFAULT "user"
);

DROP TABLE IF EXISTS categories;

CREATE TABLE IF NOT EXISTS categories (
	id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);	

DROP TABLE IF EXISTS activities;

CREATE TABLE IF NOT EXISTS activities (
	id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000)
);

DROP TABLE IF EXISTS categories_m2m_activities;

CREATE TABLE IF NOT EXISTS categories_m2m_activities (
	category_id INT NOT NULL,
    activity_id INT NOT NULL,
    PRIMARY KEY (category_id, activity_id),
    CONSTRAINT fk_category_id 
		FOREIGN KEY (category_id) 
        REFERENCES categories(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
	CONSTRAINT fk_category_activity_id 
		FOREIGN KEY (activity_id)
        REFERENCES activities(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

DROP TABLE IF EXISTS users_m2m_activities;

CREATE TABLE IF NOT EXISTS users_m2m_activities (
	user_id INT NOT NULL,
    activity_id INT NOT NULL,
    accepted TINYINT DEFAULT 0,
    minutes_spent INT UNSIGNED DEFAULT 0,
    requested_abandon TINYINT DEFAULT 0,
	PRIMARY KEY (user_id, activity_id),
    CONSTRAINT fk_user_id 
		FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
	CONSTRAINT fk_user_activity_id 
		FOREIGN KEY (activity_id) 
        REFERENCES activities(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

INSERT INTO users VALUES (DEFAULT, "admin1", "admin", "admin", "admin", "admin");
SELECT * from users;

INSERT INTO activities VALUES (DEFAULT, "act1", "desc1");
SELECT * FROM activities;

INSERT INTO categories VALUES (DEFAULT, "cat3"), (DEFAULT, "cat2");
SELECT * FROM categories;

INSERT INTO categories_m2m_activities VALUES (1, 1), (2, 1), (3, 1);
SELECT * FROM categories_m2m_activities;