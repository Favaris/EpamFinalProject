package com.prusan.finalproject.db.service.implementor;

import com.prusan.finalproject.db.dao.ActivityDAO;
import com.prusan.finalproject.db.dao.CategoryDAO;
import com.prusan.finalproject.db.dao.DAOException;
import com.prusan.finalproject.db.dao.UserDAO;
import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.DependencyAlreadyExistsException;
import com.prusan.finalproject.db.service.exception.NameIsTakenException;
import com.prusan.finalproject.db.service.exception.NoSuchActivityException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.DBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Activity Service default implementor. Uses DBUtils.getConnection() connection. For it to work properly, it is needed to set all dao implementor fields on this object.
 */
public class ActivityServiceImpl implements ActivityService {
    private static final Logger log = LogManager.getLogger(ActivityServiceImpl.class);
    private final DBUtils dbUtils = DBUtils.getInstance();

    private ActivityDAO activityDAO;
    private CategoryDAO categoryDAO;
    private UserDAO userDAO;

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setCategoryDAO(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public void setActivityDAO(ActivityDAO activityDAO) {
        this.activityDAO = activityDAO;
    }

    /**
     * Saves given activity to the db. Also saves all its categories in the corresponding table.
     * @throws NameIsTakenException if this activity already exists.
     * @throws ServiceException if transaction was unsuccessful.
     */
    @Override
    public void save(Activity activity) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            activityDAO.add(con, activity);

            log.debug("saved an activity {}", activity);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.debug("unable to save activity {}", activity, e);
            throw new NameIsTakenException("Activity with name '" + activity.getName() + "' already exists. Please, try another name.", e);
        }
    }

    @Override
    public void update(Activity activity) throws ServiceException {
        try (Connection con = dbUtils.getConnection()){
            activityDAO.update(con, activity);

            log.debug("updated info about activity {}", activity);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("unable to update an activity {}", activity, e);
            throw new ServiceException("Can not save this activity", e);
        }
    }

    @Override
    public void addUserActivity(UserActivity ua) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
           activityDAO.addUserActivity(con, ua);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.debug("unable to add UserActivity: {}", ua, e);
            throw new DependencyAlreadyExistsException("this user activity already exists: " + ua, e);
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
    public List<Activity> getActivities(int start, int end, String orderBy, String... filterBy) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            List<Activity> list = activityDAO.getActivities(con, end, start, orderBy, filterBy);
            log.debug("retrieved a list of activities sorted by '{}', with start={}, end={}, list size={}", orderBy, start, end, list.size());

            return list;
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("unable to get a list of activities sorted by {}, start={}, end={}", orderBy, start, end, e);
            throw new ServiceException("Failed to get a list of sorted activities", e);
        }
    }

    /**
     * Returns a number of activities not taken by a certain user and filtered by some categories' ids. If the first value in 'filterBy' is 'all', then just returns a number of all activities without filtering anything.
     * @param userId an id of user for which to count activities.
     *  @param filterBy varargs of categories' ids
     */
    @Override
    public int getActivitiesCount(int userId, String... filterBy) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            int count;
            if ("all".equals(filterBy[0])) {
                count = activityDAO.getCount(con, userId);
            } else {
                count = activityDAO.getFilteredCount(con, userId, filterBy);
            }
            log.debug("received a number of activities: {}", count);
            return count;
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("unable to get activities count with filters {}", Arrays.toString(filterBy), e);
            throw new ServiceException("Failed to get activities count", e);
        }
    }

    /**
     * Gets a List of all users' activities that are not accepted or requested for abandon.
     */
    @Override
    public List<UserActivity> getUsersRequests() throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            List<UserActivity> list = activityDAO.getRequestedUserActivities(con);
            log.debug("retrieved a list with users requests with size={}", list.size());
            return list;
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("unable to get all users' requests", e);
            throw new ServiceException("unable to get all users' requests", e);
        }
    }

    /**
     * Gets a list of all user's activities that are accepted and not requested for abandonment.
     * @throws ServiceException if some connection error occurs.
     */
    @Override
    public List<UserActivity> getAllRunningUsersActivities(int userId) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            List<UserActivity> list = userDAO.getRunningActivities(con, userId);
            log.debug("got a list of running activities, list size: {}", list.size());

            return list;
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
            throw new ServiceException("unable to get the connection", throwables);
        } catch (DAOException e) {
            log.error("unable to get all running activities", e);
            throw new ServiceException("error while trying to load all running activities", e);
        }
    }

    @Override
    public UserActivity getUserActivity(int userId, int activityId) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            UserActivity ua = activityDAO.getUserActivity(con, userId, activityId);
            if (ua == null) {
                log.debug("no such user activity, userId={}, activityId={}", userId, activityId);
                throw new NoSuchActivityException("This activity does not exist");
            }
            log.debug("retrieved a user activity: {}", ua);
            return ua;
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("unable to get UserActivity by userId={}, activityId={}", userId, activityId, e);
            throw new ServiceException(e);
        }
    }

    /**
     * Updates given user activity.
     * @throws ServiceException if there are connection issues with the db.
     */
    @Override
    public void updateUserActivity(UserActivity ua) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            activityDAO.updateUserActivity(con, ua);
            log.debug("updated a user activity {}", ua);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("error in updateUserActivity({})", ua, e);
            throw new ServiceException("unable to delete user activity", e);
        }
    }

    @Override
    public void deleteUserActivity(int userId, int activityId) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            activityDAO.deleteUserActivity(con, userId, activityId);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("error in updateUserActivity(userId={}, activityId={})", userId, activityId, e);
            throw new ServiceException("unable to delete user activity", e);
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

    /**
     * Returns a list of all activities that are not taken by this user. An activity is 'taken' by a certain user even if it is not yet accepted by an admin.
     */
    @Override
    public List<Activity> getActivitiesNotTakenByUser(int userId, int start, int end, String orderBy, String... filterBy) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            List<Activity> result = activityDAO.getAvailableActivitiesForUserId(con, userId, end, start, orderBy, filterBy);
            log.debug("received a list of all activities that are not taken by this user, list size={}", result.size());
            return result;
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("error while trying to download all activities and user's activities, userId={}", userId, e);
            throw new ServiceException("Can not download a list of activities", e);
        }
    }

}
