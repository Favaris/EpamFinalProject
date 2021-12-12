package com.prusan.finalproject.db.dao;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.UserActivity;

import java.sql.Connection;
import java.util.List;

/**
 * DAO class that defines methods applicable only to Activity entity.
 */
public interface ActivityDAO extends BasicDAO<Activity> {

    /**
     * Returns a list of activities sorted and filtered by given parameters.<br>
     * @param limit start position
     * @param offset end position
     * @param orderBy name of a certain attr in the 'activities' table to be sorted by
     */
    List<Activity> getActivities(Connection con, int limit, int offset, String orderBy, String... filterBy) throws DAOException;

    /**
     * @return  a number of all activities that the given user has.
     */
    int getCount(Connection con, int userId) throws DAOException;


    /**
     * Returns a list of all activities that are available for this user. 'Available' activity means that there are no rows in users_m2m_activities that contain both userID and this activityID.
     * @throws DAOException if connection error occurs.
     */
    List<Activity> getAvailableActivitiesForUserId(Connection con, int userId, int limit, int offset, String orderBy, String... filterBy) throws DAOException;


    /**
     * Returns count of all activities that has the given user AND pass the filters.
     * @param userId user's ID
     * @param filterBy a set of filters
     * @throws DAOException if there were some computational errors.
     */
    int getFilteredCount(Connection con, int userId, String... filterBy) throws DAOException;
}
