package com.prusan.finalproject.db.service;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.exception.ServiceException;

import java.util.List;
import java.util.Map;

/**
 * Service for managing activities.
 */
public interface ActivityService {
    void save(Activity activity) throws ServiceException;

    void addUserActivity(UserActivity ua) throws ServiceException;

    Activity getById(int id) throws ServiceException;

    List<Activity> getAll() throws ServiceException;

    List<UserActivity> getUsersRequests() throws ServiceException;

    List<UserActivity> getAllRunningUsersActivities(int userId) throws ServiceException;

    UserActivity getUserActivity(int userId, int activityId) throws ServiceException;

    void updateUserActivity(UserActivity ua) throws ServiceException;

    void deleteUserActivity(int userId, int activityId) throws ServiceException;

    void delete(int id) throws ServiceException;
}
