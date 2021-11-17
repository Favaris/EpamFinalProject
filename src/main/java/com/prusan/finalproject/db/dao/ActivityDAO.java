package com.prusan.finalproject.db.dao;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.Category;

import java.sql.Connection;
import java.util.List;

/**
 * DAO class that defines methods applicable only to Activity entity.
 */
public abstract class ActivityDAO extends BasicDAO<Activity> {
    public abstract Activity getByName(Connection con, String name) throws DAOException;

    public abstract void addCategory(Connection con, int categoryId, int activityId) throws DAOException;

    public abstract List<Integer> getAllCategoriesIds(Connection con, int id) throws DAOException;
}
