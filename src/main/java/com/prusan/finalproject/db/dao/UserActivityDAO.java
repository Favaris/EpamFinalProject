package com.prusan.finalproject.db.dao;

import com.prusan.finalproject.db.entity.UserActivity;

import java.sql.Connection;
import java.util.List;

/**
 * Interface for manipulations with UserActivity entities.
 */
public interface UserActivityDAO extends BasicDAO<UserActivity> {

    /**
     * @return a UserActivity instance by the given parameters or null if there is no such user activity
     * @throws DAOException if some connection issues occurred.
     */
    UserActivity get(Connection con, int userId, int activityId) throws DAOException;

    /**
     * removes a user activity by given parameters
     * @throws DAOException if some connection issues occurred.
     */
    void remove(Connection con, int userId, int activityId) throws DAOException;

    /**
     * Returns a result of {@link #getRequestedUserActivities(Connection, Integer, int, int)} with a null for the userId parameter.
     */
    List<UserActivity> getRequestedUserActivities(Connection con, int limit, int offset) throws DAOException;

    /**
     * Returns a list of user activities that are not accepted or requested for abandonment for user specified by a userId.<br>
     * If a userId param was null, returns a list of all requested user activities for all users.
     * @param userId id for whom to download the requests
     */
    List<UserActivity> getRequestedUserActivities(Connection con, Integer userId, int limit, int offset) throws DAOException;

    /**
     * Returns a result of {@link #getRequestsCount(Connection, Integer)} with a null for the userId parameter.
     */
    int getRequestsCount(Connection con) throws DAOException;

    /**
     * Returns a count of requests for specified user id. If id was null, returns count of all users' requests.
     */
    int getRequestsCount(Connection con, Integer userId) throws DAOException;

    /**
     * @return a number of user activities for a specific user, filtered out by the given set of filters.
     * @throws DAOException if some connection issues occurred.
     */
    int getCountByUserId(Connection con, int userId, String[] filterBy) throws DAOException;

    /**
     * @param limit max amount of entities to return
     * @param offset index to start from when making a sublist
     * @return a list of user activities for a specific user with some ordering/filtering rules.
     * @throws DAOException
     */
    List<UserActivity> getAcceptedByUserId(Connection con, int userId, int limit, int offset, String orderBy, String... filterBy) throws DAOException;

    /**
     * THIS METHOD IS NOT SUPPORTED. USE INSTEAD {@link #get(Connection, int, int)}
     * @throws UnsupportedOperationException this method is not supported for UserActivities.
     */
    @Override
    default UserActivity get(Connection con, int id) throws DAOException {
        throw new UnsupportedOperationException("get() by only one id is unsupported. Try calling get() with two ids instead");
    }

    /**
     * THIS METHOD IS NOT SUPPORTED. USE INSTEAD {@link #remove(Connection, int, int)}
     * @throws UnsupportedOperationException this method is not supported for UserActivities.
     */
    @Override
    default void remove(Connection con, int id) throws DAOException {
        throw new UnsupportedOperationException("remove() by only one id is unsupported. Try calling remove() with two ids instead");
    }
}