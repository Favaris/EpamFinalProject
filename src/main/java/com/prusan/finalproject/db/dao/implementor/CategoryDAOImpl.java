package com.prusan.finalproject.db.dao.implementor;

import com.prusan.finalproject.db.dao.CategoryDAO;
import com.prusan.finalproject.db.dao.DAOException;
import com.prusan.finalproject.db.entity.Category;

import java.sql.Connection;
import java.util.List;

public class CategoryDAOImpl extends CategoryDAO {
    @Override
    public void add(Connection con, Category category) throws DAOException {

    }

    @Override
    public List<Category> getAll(Connection con) throws DAOException {
        return null;
    }

    @Override
    public Category get(Connection con, int id) throws DAOException {
        return null;
    }

    @Override
    public void update(Connection con, Category category) throws DAOException {

    }

    @Override
    public void remove(Connection con, int id) throws DAOException {

    }

    @Override
    public Category getByName(Connection con, String name) throws DAOException {
        return null;
    }
}
