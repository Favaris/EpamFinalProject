package com.prusan.finalproject.db.dao;

import com.prusan.finalproject.db.entity.User;

import java.sql.Connection;
import java.util.List;

/**
 * DAO class that defines methods applicable only to User entity.
 */
public interface UserDAO extends BasicDAO<User> {
    User getByLogin(Connection con,  String login) throws DAOException;

    User getByLoginAndPassword(Connection con, String login, String password) throws DAOException;

    /**
     * @param limit 'LIMIT' value in SQL query
     * @param offset 'OFFSET' value in SQL query
     * @param like 'LIKE' value in SQL query
     * @return a list of users with role='user', filtered and ordered by passed rules.
     * @throws DAOException if there are some issues with the connection to the db.
     */
    List<User> getWithRoleUser(Connection con, int limit, int offset, String orderBy, String countLessThan, String countBiggerThan, String like) throws DAOException;

    /**
     * @param countLessThan user's activities count is less than this value
     * @param countBiggerThan user's activities count is bigger than this value
     * @param like 'LIKE' value in SQL query
     * @throws DAOException if there are some issues with the connection to the db.
     */
    int getCountWithRoleUser(Connection con, String countLessThan, String countBiggerThan, String like) throws DAOException;
}
