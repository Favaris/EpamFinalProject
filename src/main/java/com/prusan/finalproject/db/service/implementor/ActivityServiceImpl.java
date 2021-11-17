package com.prusan.finalproject.db.service.implementor;

import com.prusan.finalproject.db.dao.ActivityDAO;
import com.prusan.finalproject.db.dao.CategoryDAO;
import com.prusan.finalproject.db.dao.DAOException;
import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.NameIsTakenException;
import com.prusan.finalproject.db.service.exception.NoSuchActivityException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.DBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity Service default implementor. Uses DBUtils.getConnection() connection. For it to work properly, it is needed to set all dao implementor fields on this object.
 */
public class ActivityServiceImpl implements ActivityService {
    private static final Logger log = LogManager.getLogger(ActivityServiceImpl.class);
    private final DBUtils dbUtils = DBUtils.getInstance();

    private ActivityDAO activityDAO;
    private CategoryDAO categoryDAO;

    public void setCategoryDAO(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public void setActivityDAO(ActivityDAO activityDAO) {
        this.activityDAO = activityDAO;
    }

    /**
     * Saves given activity to the db. Also saves all its categories in the corresponding table.
     * @throws ServiceException if transaction was unsuccessful.
     */
    @Override
    public void save(Activity activity) throws ServiceException {
        Connection con = null;
        try {
            con = dbUtils.getConnection(false);
            activityDAO.add(con, activity);

            for (Category category : activity.getCategories()) {
                activityDAO.addCategory(con, category.getId(), activity.getId());
            }
            log.debug("saved an activity {}", activity);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            rollback(con);
            log.debug("unable to save activity {}", activity, e);
            throw new NameIsTakenException("Activity with name '" + activity.getName() + "' already exists. Please, try another name.", e);
        }
    }

    @Override
    public void addCategories(List<Category> categories) throws ServiceException {
        try (Connection con = dbUtils.getConnection(false)) {

        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        }
    }

    /**
     * Returns an Activity object by its id.
     * @throws ServiceException
     */
    @Override
    public Activity getById(int id) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            Activity ac = activityDAO.get(con, id);
            setCategories(con, ac);

            log.debug("retrieved an activity by id {}", ac);
            return ac;
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("unable to get activity by id={}", id, e);
            throw new NoSuchActivityException("unable to get an activity with id=" + id, e);
        }
    }

    @Override
    public List<Activity> getAll() throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            List<Activity> activities = activityDAO.getAll(con);

            for (Activity activity : activities) {
                setCategories(con, activity);
            }

            log.debug("retrieved a list of all activities, list size: {}", activities.size());
            return activities;
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("unable to get all activities", e);
            throw new ServiceException("unable to get all activities", e);
        }
    }

    @Override
    public void delete(int id) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            activityDAO.remove(con, id);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("unable to delete an activity by id={}", id, e);
            throw new NoSuchActivityException("activity with id="+ id + "does not exist", e);
        }
    }

    private void setCategories(Connection con, Activity ac) throws DAOException {
        List<Integer> catIds = activityDAO.getAllCategoriesIds(con, ac.getId());

        List<Category> categories = new ArrayList<>();
        for (Integer catId : catIds) {
            categories.add(categoryDAO.get(con, catId));
        }

        ac.setCategories(categories);
        log.debug("retrieved a list of categories for activity, list size: {}", categories.size());
    }

    private void rollback(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                log.error("unable to rollback connection", ex);
            }
        }
    }
}
