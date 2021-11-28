package com.prusan.finalproject.db.dao;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.UserActivity;

import java.sql.Connection;
import java.util.List;

/**
 * DAO class that defines methods applicable only to Activity entity.
 */
public abstract class ActivityDAO extends BasicDAO<Activity> {
    public abstract Activity getByName(Connection con, String name) throws DAOException;

    public abstract void addCategory(Connection con, int categoryId, int activityId) throws DAOException;

    public abstract List<Activity> getActivities(Connection con, int limit, int offset, String orderBy) throws DAOException;

    public abstract int getCount(Connection con) throws DAOException;

    public abstract List<Integer> getCategoriesIds(Connection con, int id) throws DAOException;

    public abstract void deleteAllCategories(Connection con, int activityId) throws DAOException;

    public abstract void addUserActivity(Connection con, UserActivity ua) throws DAOException;

    public abstract UserActivity getUserActivity(Connection con, int userId, int activityId) throws DAOException;

    public abstract void updateUserActivity(Connection con, UserActivity ua) throws DAOException;

    public abstract void deleteUserActivity(Connection con, int userId, int activityId) throws DAOException;

    public abstract List<UserActivity> getRequestedUserActivities(Connection con) throws DAOException;

    public abstract List<Activity> getActivitiesByUserId(Connection con, int userId) throws DAOException;
}
