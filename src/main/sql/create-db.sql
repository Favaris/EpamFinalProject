DROP DATABASE IF EXISTS time_accounting_db;
CREATE DATABASE time_accounting_db;
USE time_accounting_db;

DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
	u_id INT PRIMARY KEY AUTO_INCREMENT,
    u_login VARCHAR(16) NOT NULL UNIQUE,
    u_password VARCHAR(32) NOT NULL,
    u_name VARCHAR(30) NOT NULL,
    u_surname VARCHAR(30) NOT NULL,
    u_role ENUM('user', 'admin') DEFAULT 'user'
);

DROP TABLE IF EXISTS categories;

CREATE TABLE IF NOT EXISTS categories (
	c_id INT PRIMARY KEY AUTO_INCREMENT,
    c_name VARCHAR(255) UNIQUE NOT NULL
);	

DROP TABLE IF EXISTS activities;

CREATE TABLE IF NOT EXISTS activities (
	a_id INT PRIMARY KEY AUTO_INCREMENT,
	a_category_id INT NOT NULL,
    a_name VARCHAR(30) UNIQUE NOT NULL,
    a_description VARCHAR(1000),
    a_users_count INT UNSIGNED DEFAULT 0,
    CONSTRAINT fk_category_id
        FOREIGN KEY (a_category_id)
        REFERENCES categories(c_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

DROP TABLE IF EXISTS users_m2m_activities;

CREATE TABLE IF NOT EXISTS users_m2m_activities (
	ua_user_id INT NOT NULL,
    ua_activity_id INT NOT NULL,
    ua_accepted BIT DEFAULT 0,
    ua_minutes_spent INT UNSIGNED DEFAULT 0,
    ua_requested_abandon BIT DEFAULT 0,
	PRIMARY KEY (ua_user_id, ua_activity_id),
    CONSTRAINT fk_user_id 
		FOREIGN KEY (ua_user_id)
        REFERENCES users(u_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
	CONSTRAINT fk_user_activity_id 
		FOREIGN KEY (ua_activity_id) 
        REFERENCES activities(a_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

INSERT INTO users VALUES (DEFAULT, 'admin', 'admin', 'admin', 'admin', 'admin');
SELECT * from users;

/*Triggers for automatic users count for every activity*/
DROP TRIGGER IF EXISTS increment_users_count;

CREATE TRIGGER increment_users_count AFTER INSERT ON users_m2m_activities
FOR EACH ROW UPDATE activities SET a_users_count = a_users_count + 1 WHERE a_id = NEW.ua_activity_id;

DROP TRIGGER IF EXISTS decrement_users_count;

CREATE TRIGGER decrement_users_count AFTER DELETE ON users_m2m_activities
FOR EACH ROW UPDATE activities SET a_users_count = a_users_count - 1 WHERE a_id = OLD.ua_activity_id;