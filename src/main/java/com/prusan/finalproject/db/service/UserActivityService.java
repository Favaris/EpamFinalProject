package com.prusan.finalproject.db.service;

import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.exception.ServiceException;

import java.util.List;

public interface UserActivityService extends BasicService<UserActivity> {

    void add(UserActivity ua) throws ServiceException;

    void update(UserActivity ua) throws ServiceException;

    void delete(int userId, int activityId) throws ServiceException;

    UserActivity get(int userId, int activityId) throws ServiceException;

    default void delete(int id) {
        throw new UnsupportedOperationException("You need to use 2 ids to delete a user activity");
    }

    /**
     * Gets a List of all users' activities that are not accepted or requested for abandon.
     */
    List<UserActivity> getRequestedActivitiesForAllUsers(int start, int amount) throws ServiceException;

    /**
     * Gets a List of all accepted activities for  ordered and filtered by passed parameters.
     */
    List<UserActivity> getAcceptedForUser(int userId, int start, int amount, String orderBy, String[] filterBy) throws ServiceException;

    /**
     * Gets a number of user's activities that pass given filters
     */
    int getActivitiesCountForUser(int userId, String... filterBy) throws ServiceException;

    /**
     * Gets a List of user's activities that are not accepted or requested for abandonment,
     */
    List<UserActivity> getRequestedActivitiesForUser(Integer userId, int start, int amount) throws ServiceException;

    /**
     * @return a number of user's activities that are not accepted or requested for abandonment for the given user.
     */
    int getRequestsCountForUser(Integer userId) throws ServiceException;

    /**
     * @return a number of user's activities that are not accepted or requested for abandonment for all users.
     */
    int getRequestsCountForAdmin() throws ServiceException;
}
