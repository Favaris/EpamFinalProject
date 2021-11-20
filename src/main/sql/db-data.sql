DROP PROCEDURE IF EXISTS fill_users;

delimiter //
CREATE PROCEDURE fill_users(count INT) 
BEGIN
	WHILE count > 0 DO
		INSERT INTO users(login, password, name, surname) VALUES (concat('user', count), 1234, concat('name', count), concat('фамилия', count));
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
		INSERT INTO categories(name) VALUES(concat('Category', count));
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
		INSERT INTO activities(name, description) VALUES(concat('Activity', count), concat(count, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.'));
        SET count = count - 1;
    END WHILE;
END//
delimiter ;

DELETE FROM activities;

CALL fill_activities(20);
SELECT * FROM activities;

DROP PROCEDURE IF EXISTS fill_categories_m2m_activities;

delimiter //
CREATE PROCEDURE fill_categories_m2m_activities()
BEGIN 
	SELECT COUNT(*) INTO @act_count FROM activities;
    SELECT COUNT(*) INTO @cat_count FROM categories;
    SET @cat = @cat_count;
    WHILE @act_count > 0 DO 
		SELECT id INTO @act_id FROM activities WHERE name = concat('Activity', @act_count);
        SELECT id INTO @cat1_id FROM categories WHERE name = concat('Category', @cat);
        IF @cat = 1 THEN 
			SET @cat = @cat_count;
		ELSE 
			SET @cat = @cat - 1;
		END IF;
		SELECT id INTO @cat2_id FROM categories WHERE name = concat('Category', @cat);
        INSERT INTO categories_m2m_activities VALUES (@cat1_id, @act_id), (@cat2_id, @act_id);
        SET @act_count = @act_count - 1;
    END WHILE;
END//
delimiter ;

DELETE FROM categories_m2m_activities;

CALL fill_categories_m2m_activities();
SELECT * FROM categories_m2m_activities;