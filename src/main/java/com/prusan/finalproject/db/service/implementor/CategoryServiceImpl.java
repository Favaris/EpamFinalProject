package com.prusan.finalproject.db.service.implementor;

import com.prusan.finalproject.db.dao.CategoryDAO;
import com.prusan.finalproject.db.dao.DAOException;
import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.service.CategoryService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.DBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Category service implementor. For it to work properly, you must set all its DAO fields first.
 */
public class CategoryServiceImpl implements CategoryService {
    private static final Logger log = LogManager.getLogger(CategoryServiceImpl.class);
    private final DBUtils dbUtils = DBUtils.getInstance();

    private CategoryDAO categoryDAO;

    public void setCategoryDAO(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @Override
    public List<Category> getAll() throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            List<Category> categories = categoryDAO.getAll(con);
            log.debug("got a list of all categories, list size: {}", categories.size());
            return categories;
        } catch (DAOException e) {
            log.error("error in getAll() ", e);
            throw new ServiceException(e);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("can not get connection with the db", throwables);
        }
    }
}
