package com.prusan.finalproject.db.dao;

import com.prusan.finalproject.db.entity.Activity;

import java.sql.Connection;

/**
 * DAO class that defines methods applicable only to Activity entity.
 */
public abstract class ActivityDAO extends BasicDAO<Activity> {
    public abstract Activity getByName(Connection con, String name) throws DAOException;
}
