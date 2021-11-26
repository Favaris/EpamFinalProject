package com.prusan.finalproject.db.service;

import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.service.exception.ServiceException;

import java.util.List;

/**
 * Service class for managing the categories.
 */
public interface CategoryService {
    List<Category> getAll() throws ServiceException;

    void save(Category category) throws ServiceException;

    void update(Category category) throws ServiceException;
}
