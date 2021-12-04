package com.prusan.finalproject.db.dao;

import com.prusan.finalproject.db.entity.Category;

import java.sql.Connection;
import java.util.List;

/**
 * DAO class that defines methods applicable only to Category entity.
 */
public abstract class CategoryDAO extends BasicDAO<Category> {
    public abstract List<Category> getCategories(Connection con, int limit, int offset) throws DAOException;

    public abstract int getCount(Connection con) throws DAOException;
}
