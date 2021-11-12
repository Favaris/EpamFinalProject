package com.prusan.finalproject.db.dao.implementor;

import com.prusan.finalproject.db.dao.ActivityDAO;
import com.prusan.finalproject.db.dao.DAOException;
import com.prusan.finalproject.db.entity.Activity;

import java.sql.Connection;
import java.util.List;

public class ActivityDAOImpl extends ActivityDAO {
    @Override
    public Activity getByName(Connection con, String name) throws DAOException {
        return null;
    }

    @Override
    public void add(Connection con, Activity activity) throws DAOException {

    }

    @Override
    public List<Activity> getAll(Connection con) throws DAOException {
        return null;
    }

    @Override
    public Activity get(Connection con, int id) throws DAOException {
        return null;
    }

    @Override
    public void update(Connection con, Activity activity) throws DAOException {

    }

    @Override
    public void remove(Connection con, int id) throws DAOException {

    }
}
