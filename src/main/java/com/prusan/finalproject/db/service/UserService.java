package com.prusan.finalproject.db.service;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.service.exception.ServiceException;

import java.util.List;

public interface UserService {
    void save(User user) throws ServiceException;

    void update(User user) throws ServiceException;

    User getById(int id) throws ServiceException;

    User getByLoginAndPass(String login, String pass) throws ServiceException;

    List<User> getWithRoleUser(int start, int amount, String orderBy, String countLessThen, String countBiggerThen, String searchBy) throws ServiceException;

    int getDefaultUsersCount() throws ServiceException;

    void delete(int id) throws ServiceException;
}
