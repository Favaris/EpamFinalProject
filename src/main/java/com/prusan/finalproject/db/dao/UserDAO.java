package com.prusan.finalproject.db.dao;

import com.prusan.finalproject.db.entity.User;

import java.sql.Connection;

/**
 * DAO class that defines methods applicable only to User entity.
 */
public abstract class UserDAO extends BasicDAO<User> {
    public abstract User getByLogin(Connection con,  String login) throws DAOException;
    public abstract User getByLoginAndPassword(Connection con, String login, String password) throws DAOException;
}
