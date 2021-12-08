package com.prusan.finalproject.db.dao;

import com.prusan.finalproject.db.entity.User;

import java.sql.Connection;
import java.util.List;

/**
 * DAO class that defines methods applicable only to User entity.
 */
public abstract class UserDAO extends BasicDAO<User> {
    public abstract User getByLogin(Connection con,  String login) throws DAOException;

    public abstract User getByLoginAndPassword(Connection con, String login, String password) throws DAOException;

    public abstract List<User> getWithRoleUser(Connection con, int limit, int offset) throws DAOException;

    public abstract int getCountWithRoleUser(Connection con) throws DAOException;
}
