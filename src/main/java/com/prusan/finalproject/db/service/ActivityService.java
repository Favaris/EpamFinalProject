package com.prusan.finalproject.db.service;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.exception.ServiceException;

import java.util.List;
import java.util.Map;

/**
 * Service for managing activities.
 */
public interface ActivityService extends BasicService<Activity> {

    /**
     * Returns a list of all activities that are available for this user. 'Available' activity means that there are no rows in users_m2m_activities that contain both userID and this activityID.
     */
    List<Activity> getActivitiesNotTakenByUser(int userId, int start, int amount, String orderBy, String... filterBy) throws ServiceException;

    /**
     * Retrieves a list of activities filtered/ordered/cut by given parameters
     * @param start starting element index
     * @param amount max number of entities
     * @param orderBy ordering rule
     * @param filterBy filter sets
     * @throws ServiceException if operation was unsuccessful
     */
    List<Activity> getActivities(int start, int amount, String orderBy, String... filterBy) throws ServiceException;


    /**
     * Returns a number of activities not taken by a certain user and filtered by some categories' ids. If the first value in 'filterBy' is 'all', then just returns a number of all activities without filtering anything.
     * @param userId an id of user for which to count activities.
     *  @param filterBy varargs of categories' ids
     */
    int getActivitiesCount(int userId, String... filterBy) throws ServiceException;
}
