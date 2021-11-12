package com.prusan.finalproject.db.dao;

import com.prusan.finalproject.db.entity.Category;

import java.sql.Connection;

/**
 * DAO class that defines methods applicable only to Category entity.
 */
public abstract class CategoryDAO extends BasicDAO<Category> {
    public abstract Category getByName(Connection con, String name) throws DAOException;
}
