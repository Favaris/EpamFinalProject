package com.prusan.finalproject.db.service;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.service.exception.ServiceException;

import java.util.List;

public interface UserService extends BasicService<User> {
    User getById(int id) throws ServiceException;

    User getByLoginAndPass(String login, String pass) throws ServiceException;

    List<User> getWithRoleUser(int start, int amount, String orderBy, String countLessThan, String countBiggerThan, String searchBy) throws ServiceException;

    int getDefaultUsersCount(String countLessThan, String countBiggerThan, String searchBy) throws ServiceException;
}
