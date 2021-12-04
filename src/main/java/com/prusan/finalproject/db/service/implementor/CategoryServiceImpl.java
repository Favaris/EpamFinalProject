package com.prusan.finalproject.db.service.implementor;

import com.prusan.finalproject.db.dao.CategoryDAO;
import com.prusan.finalproject.db.dao.DAOException;
import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.service.CategoryService;
import com.prusan.finalproject.db.service.exception.FailedCategoryDeletionException;
import com.prusan.finalproject.db.service.exception.NameIsTakenException;
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
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
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

    @Override
    public List<Category> getCategories(int start, int end) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            List<Category> categories = categoryDAO.getCategories(con, end, start);
            log.debug("received a list of categories, start={}, end={}, list size: {}", start, end, categories.size());
            return categories;
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("can not get connection with the db", throwables);
        } catch (DAOException e) {
            log.error("failed to get all categories with start={}, end={}", start, end, e);
            throw new ServiceException("Failed to get a list of categories", e);
        }
    }

    @Override
    public int getCount() throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            int count = categoryDAO.getCount(con);
            log.debug("received a categories' count: {}", count);
            return count;
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("can not get connection with the db", throwables);
        } catch (DAOException e) {
            log.error("failed to get categories' count", e);
            throw new ServiceException("Failed to get the amount of categories", e);
        }
    }

    @Override
    public void save(Category category) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            categoryDAO.add(con, category);
            log.debug("successfully added a new category {}", category);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("can not get connection with the db", throwables);
        } catch (DAOException e) {
            log.error("unable to save a new category {}", category, e);
            throw new NameIsTakenException("Category with name '" + category.getName() + "' already exists", e);
        }
    }

    @Override
    public void update(Category category) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            categoryDAO.update(con, category);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("can not get connection with the db", throwables);
        } catch (DAOException e) {
            log.error("unable to update a category {}", category, e);
            throw new NameIsTakenException("Can not update this category: category with name '" + category.getName() + "' already exists", e);
        }
    }

    @Override
    public void delete(int id) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            categoryDAO.remove(con, id);
            log.debug("removed a category by id={}", id);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("can not get connection with the db", throwables);
        } catch (DAOException e) {
            log.error("failed to delete a category");
            throw new FailedCategoryDeletionException("Can not delete this category. Please, make sure that there are no activities associated with this category.", e);
        }
    }
}
