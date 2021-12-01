package com.prusan.finalproject.db.service;

import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.exception.ServiceException;

import java.util.List;

public interface UserActivityService {

    List<UserActivity> getUsersRequests() throws ServiceException;

    void save(UserActivity ua) throws ServiceException;

    List<UserActivity> getAllAcceptedForUser(int userId) throws ServiceException;

    UserActivity get(int userId, int activityId) throws ServiceException;

    void update(UserActivity ua) throws ServiceException;

    void delete(int userId, int activityId) throws ServiceException;
}