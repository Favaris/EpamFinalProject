DROP DATABASE IF EXISTS time_accounting_db;
CREATE DATABASE time_accounting_db;
USE time_accounting_db;

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
    name VARCHAR(255) UNIQUE NOT NULL
);	

DROP TABLE IF EXISTS activities;

CREATE TABLE IF NOT EXISTS activities (
	id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30) UNIQUE NOT NULL,
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
    accepted BIT DEFAULT 0,
    minutes_spent INT UNSIGNED DEFAULT 0,
    requested_abandon BIT DEFAULT 0,
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

INSERT INTO users VALUES (DEFAULT, "admin", "admin", "admin", "admin", "admin");
SELECT * from users;
