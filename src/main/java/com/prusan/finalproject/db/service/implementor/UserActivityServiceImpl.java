package com.prusan.finalproject.db.service.implementor;

import com.prusan.finalproject.db.dao.DAOException;
import com.prusan.finalproject.db.dao.UserActivityDAO;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.UserActivityService;
import com.prusan.finalproject.db.service.exception.DependencyAlreadyExistsException;
import com.prusan.finalproject.db.service.exception.NoSuchActivityException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.DBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class UserActivityServiceImpl implements UserActivityService {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private final DBUtils dbUtils = DBUtils.getInstance();
    private UserActivityDAO userActivityDAO;
    public void setUserActivityDAO(UserActivityDAO userActivityDAO) {
        this.userActivityDAO = userActivityDAO;
    }

    @Override
    public void add(UserActivity ua) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            userActivityDAO.add(con, ua);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.debug("unable to add UserActivity: {}", ua, e);
            throw new DependencyAlreadyExistsException("this user activity already exists: " + ua, e);
        }
    }

    @Override
    public List<UserActivity> getRequestedActivitiesForAllUsers(int start, int amount) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            List<UserActivity> list = userActivityDAO.getRequestedUserActivities(con, amount, start);
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


    @Override
    public List<UserActivity> getAcceptedForUser(int userId, int start, int amount, String orderBy, String[] filterBy) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            List<UserActivity> list = userActivityDAO.getAcceptedByUserId(con, userId, amount, start, orderBy, filterBy);
            log.debug("received a list of user activities for user #{} with start={}, end={}, orderBy={}, filterBy={}", userId, start, amount, orderBy, Arrays.toString(filterBy));
            return list;
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
            throw new ServiceException("unable to get the connection", throwables);
        } catch (DAOException e) {
            log.error("unable to get user activities by userId={}, start={}, end={}", userId, start, amount, e);
            throw new ServiceException("Failed to download user's activities", e);
        }
    }

    @Override
    public int getActivitiesCountForUser(int userId, String... filterBy) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            int count = userActivityDAO.getCountByUserId(con, userId, filterBy);
            log.debug("received amount of user's activities: {}", count);
            return count;
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
            throw new ServiceException("unable to get the connection", throwables);
        } catch (DAOException e) {
            log.error("failed to receive a count of user's activities by userId={}", userId, e);
            throw new ServiceException("Failed to get amount of user's activities", e);
        }
    }


    @Override
    public UserActivity get(int userId, int activityId) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            UserActivity ua = userActivityDAO.get(con, userId, activityId);
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
    public void update(UserActivity ua) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            userActivityDAO.update(con, ua);
            log.debug("updated a user activity {}", ua);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("error in update({})", ua, e);
            throw new ServiceException("unable to delete user activity", e);
        }
    }

    @Override
    public void delete(int userId, int activityId) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            userActivityDAO.remove(con, userId, activityId);
            log.debug("deleted a user activity by userId={}, activityId={}", userId, activityId);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("error in delete(userId={}, activityId={})", userId, activityId, e);
            throw new ServiceException("unable to delete user activity", e);
        }
    }

    @Override
    public List<UserActivity> getRequestedActivitiesForUser(Integer userId, int start, int amount) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            List<UserActivity> requested = userActivityDAO.getRequestedUserActivities(con, userId, amount, start);
            log.debug("received a list of requested activities for userId={}, list size: {}", userId, requested.size());
            return requested;
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("failed to get requested activities for user #{}, start={}, amount={}", userId, start, amount, e);
            throw new ServiceException("Failed to download requests");
        }
    }

    @Override
    public int getRequestsCountForUser(Integer userId) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            int count = userActivityDAO.getRequestsCount(con, userId);
            log.debug("received a requests amount for userId={}, amount={}", userId, count);
            return count;
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("failed to get requests count for user id={}", userId);
            throw new ServiceException("Failed to get amount of requests");
        }
    }

    @Override
    public int getRequestsCountForAdmin() throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            int count = userActivityDAO.getRequestsCount(con);
            log.debug("received a requests amount for admin, amount={}", count);
            return count;
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("failed to get requests count for admin");
            throw new ServiceException("Failed to get amount of requests");
        }
    }
}
