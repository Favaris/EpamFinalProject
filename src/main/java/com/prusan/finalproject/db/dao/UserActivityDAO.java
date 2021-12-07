package com.prusan.finalproject.db.dao;

import com.prusan.finalproject.db.entity.UserActivity;

import java.sql.Connection;
import java.util.List;

public abstract class UserActivityDAO extends BasicDAO<UserActivity> {

    public abstract List<UserActivity> getAcceptedByUserId(Connection con, int id) throws DAOException;

    public abstract UserActivity get(Connection con, int userId, int activityId) throws DAOException;

    public abstract void remove(Connection con, int userId, int activityId) throws DAOException;

    public abstract List<UserActivity> getRequestedUserActivities(Connection con) throws DAOException;

    /**
     * Removes all user's activities by given user id.
     * @throws DAOException when some connection issues happen
     */
    public abstract void removeAllByUserId(Connection con, int userId) throws DAOException;

    public abstract int getCountByUserId(Connection con, int userId, String[] filterBy) throws DAOException;

    public abstract int getSummarizedSpentTimeByUserId(Connection con, int userId) throws DAOException;

    public abstract List<UserActivity> getAcceptedByUserId(Connection con, int userId, int limit, int offset, String orderBy, String... filterBy) throws DAOException;

    @Override
    public List<UserActivity> getAll(Connection con) throws DAOException {
        throw new UnsupportedOperationException("getAll() is currently unsupported");
    }

    @Override
    public UserActivity get(Connection con, int id) throws DAOException {
        throw new UnsupportedOperationException("get() by only one id is unsupported. Try calling get() with two ids instead");
    }


    @Override
    public void remove(Connection con, int id) throws DAOException {
        throw new UnsupportedOperationException("remove() by only one id is unsupported. Try calling remove() with two ids instead");
    }
}