DROP PROCEDURE IF EXISTS fill_users;

delimiter //
CREATE PROCEDURE fill_users(count INT) 
BEGIN
	WHILE count > 0 DO
		INSERT INTO users(u_login, u_password, u_name, u_surname) VALUES (concat('user', count), 1234, concat('name', count), concat('фамилия', count));
        SET count = count - 1;
    END WHILE;
END//
delimiter ;

DELETE FROM users;

INSERT INTO users VALUES (DEFAULT, "admin", "admin", "admin", "admin", "admin");
CALL fill_users(15);
SELECT * FROM users;

DROP PROCEDURE IF EXISTS fill_categories;

delimiter //
CREATE PROCEDURE fill_categories(count INT) 
BEGIN
	WHILE count > 0 DO
		INSERT INTO categories(c_name) VALUES(concat('Category', count));
        SET count = count - 1;
    END WHILE;
END//
delimiter ;

DELETE FROM categories;

CALL fill_categories(10);
SELECT * FROM categories;

DROP PROCEDURE IF EXISTS fill_activities;

delimiter //
CREATE PROCEDURE fill_activities(count INT) 
BEGIN
	WHILE count > 0 DO
	    SELECT c_id INTO @cat_id FROM categories ORDER BY RAND() LIMIT 1;
		INSERT INTO activities(a_name, a_description, a_category_id) VALUES(concat('Activity', count),
		                                                              concat(count, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.'),
		                                                              @cat_id);
        SET count = count - 1;
    END WHILE;
END//
delimiter ;

DELETE FROM activities;

CALL fill_activities(20);
SELECT * FROM activities;


