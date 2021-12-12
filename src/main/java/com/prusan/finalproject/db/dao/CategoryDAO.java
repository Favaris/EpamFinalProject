package com.prusan.finalproject.db.dao;

import com.prusan.finalproject.db.entity.Category;

import java.sql.Connection;
import java.util.List;

/**
 * DAO class that defines methods applicable only to Category entity.
 */
public interface CategoryDAO extends BasicDAO<Category> {

    /**
     * Gets a list of categories bordered by given parameters.
     * @param limit max amount of elements returned.
     * @param offset index from where to start
     * @throws DAOException if there were some issues with the connection
     */
    List<Category> getCategories(Connection con, int limit, int offset) throws DAOException;

    /**
     * @return a number of all categories.
     * @throws DAOException if there were some issues with the connection
     */
    int getCount(Connection con) throws DAOException;
}
