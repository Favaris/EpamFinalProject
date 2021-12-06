package com.prusan.finalproject.db.service;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.exception.ServiceException;

import java.util.List;
import java.util.Map;

/**
 * Service for managing activities.
 */
public interface ActivityService {
    void save(Activity activity) throws ServiceException;

    void update(Activity activity) throws ServiceException;

    void delete(int id) throws ServiceException;

    List<Activity> getActivitiesNotTakenByUser(int userId, int start, int amount, String orderBy, String... filterBy) throws ServiceException;

    Activity getById(int id) throws ServiceException;

    List<Activity> getAll() throws ServiceException;

    List<Activity> getActivities(int start, int amount, String orderBy, String... filterBy) throws ServiceException;

    int getActivitiesCount(int userId, String... filterBy) throws ServiceException;
}
