package com.prusan.finalproject.db.service;

import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.service.exception.ServiceException;

import java.util.List;

public interface CategoryService {
    List<Category> getAll() throws ServiceException;
}
