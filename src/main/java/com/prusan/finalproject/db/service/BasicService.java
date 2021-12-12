package com.prusan.finalproject.db.service;

import com.prusan.finalproject.db.service.exception.ServiceException;

/**
 * Base interface for service classes.
 */
public interface BasicService <T> {
    void add(T t) throws ServiceException;

    void update(T t) throws ServiceException;

    void delete(int id) throws ServiceException;
}
