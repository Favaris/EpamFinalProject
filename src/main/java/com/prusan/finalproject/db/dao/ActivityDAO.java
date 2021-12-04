package com.prusan.finalproject.db.dao;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.UserActivity;

import java.sql.Connection;
import java.util.List;

/**
 * DAO class that defines methods applicable only to Activity entity.
 */
public abstract class ActivityDAO extends BasicDAO<Activity> {
    public abstract List<Activity> getActivities(Connection con, int limit, int offset, String orderBy, String... filterBy) throws DAOException;

    public abstract int getCount(Connection con, int userId) throws DAOException;

    public abstract List<Activity> getAvailableActivitiesForUserId(Connection con, int userId, int limit, int offset, String orderBy, String... filterBy) throws DAOException;

    public abstract int getFilteredCount(Connection con, int userId, String... filterBy) throws DAOException;
    
}
