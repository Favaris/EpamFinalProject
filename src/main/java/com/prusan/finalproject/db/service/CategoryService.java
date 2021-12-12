package com.prusan.finalproject.db.service;

import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.service.exception.ServiceException;

import java.util.List;

/**
 * Service class for managing the categories.
 */
public interface CategoryService extends BasicService<Category> {
    List<Category> getAll() throws ServiceException;

    /**
     * Returns a list of categories starting from the given index and with max size=amount
     * @throws ServiceException if operation was unsuccessful
     */
    List<Category> getCategories(int start, int amount) throws ServiceException;

    /**
     * @return a number of all categories
     * @throws ServiceException if operation was unsuccessful
     */
    int getCount() throws ServiceException;
}
