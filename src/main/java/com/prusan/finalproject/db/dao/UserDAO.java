package com.prusan.finalproject.db.dao;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.entity.UserActivity;

import java.sql.Connection;
import java.util.List;

/**
 * DAO class that defines methods applicable only to User entity.
 */
public abstract class UserDAO extends BasicDAO<User> {
    public abstract User getByLogin(Connection con,  String login) throws DAOException;

    public abstract User getByLoginAndPassword(Connection con, String login, String password) throws DAOException;

    public abstract List<UserActivity> getRunningActivities(Connection con, int id) throws DAOException;

    public abstract List<User> getAllAdmins(Connection con) throws DAOException;

    public abstract List<User> getAllWithRoleUser(Connection con) throws DAOException;
}
