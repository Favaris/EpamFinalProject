package com.prusan.finalproject.db.service;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.exception.ServiceException;

import java.util.List;

public interface UserService {
    void save(User user) throws ServiceException;

    void update(User user) throws ServiceException;

    User getById(int id) throws ServiceException;

    User getByLoginAndPass(String login, String pass) throws ServiceException;

    List<User> getWithRoleUser(int start, int end) throws ServiceException;

    List<User> getAllWithRoleUser() throws ServiceException;

    int getDefaultUsersCount() throws ServiceException;

    void delete(int id) throws ServiceException;

    void updateWithUserActivities(User u, List<UserActivity> activities) throws ServiceException;
}
