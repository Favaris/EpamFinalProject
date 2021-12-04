package com.prusan.finalproject.db.service;

import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.service.exception.ServiceException;

import java.util.List;

/**
 * Service class for managing the categories.
 */
public interface CategoryService {
    List<Category> getAll() throws ServiceException;

    List<Category> getCategories(int start, int end) throws ServiceException;

    int getCount() throws ServiceException;

    void save(Category category) throws ServiceException;

    void update(Category category) throws ServiceException;

    void delete(int id) throws ServiceException;
}
