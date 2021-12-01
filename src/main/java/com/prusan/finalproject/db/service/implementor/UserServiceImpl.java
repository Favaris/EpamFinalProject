package com.prusan.finalproject.db.service.implementor;

import com.prusan.finalproject.db.dao.ActivityDAO;
import com.prusan.finalproject.db.dao.BasicDAO;
import com.prusan.finalproject.db.dao.DAOException;
import com.prusan.finalproject.db.dao.UserDAO;
import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.exception.IncorrectCredentialsException;
import com.prusan.finalproject.db.service.exception.LoginIsTakenException;
import com.prusan.finalproject.db.service.exception.NoSuchUserException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.service.UserService;
import com.prusan.finalproject.db.util.DBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * UserService interface implementor. Uses DBUtils class for retrieving connections.
 */
public class UserServiceImpl implements UserService {
    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);
    private final DBUtils dbUtils = DBUtils.getInstance();

    private UserDAO userDAO;
    private ActivityDAO activityDAO;

    public void setActivityDAO(ActivityDAO activityDAO) {
        this.activityDAO = activityDAO;
    }

    public void setUserDAO(UserDAO dao) {
        userDAO = dao;
        log.debug("got set userDAO field: {}", dao);
    }

    /**
     * Saves given user to database. User object gets id field filled if the query was successful.
     * @param user a user to add to the db.
     */
    @Override
    public void save(User user) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            userDAO.add(con, user);
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
            throw new ServiceException(throwables);
        } catch (DAOException e) {
            log.debug("unable to add given user {} ", user, e);
            throw new LoginIsTakenException(user.getLogin());
        }
    }

    @Override
    public void update(User user) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            userDAO.update(con, user);
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
            throw new ServiceException(throwables);
        } catch (DAOException e) {
            log.error("error in #update({})", user, e);
            throw new ServiceException("Can not update a user " + user, e);
        }
    }

    @Override
    public User getById(int id) throws ServiceException {
        User u = null;
        try (Connection con = dbUtils.getConnection()) {
            u = userDAO.get(con, id);
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
        } catch (DAOException e) {
            log.warn("unable to get user with id={}", id, e);
            throw new ServiceException(e);
        }
        if (u != null) {
            return u;
        }
        throw new NoSuchUserException(id);
    }

    @Override
    public User getByLoginAndPass(String login, String pass) throws ServiceException {
        User u = null;
        try (Connection con = dbUtils.getConnection()) {
            u = userDAO.getByLoginAndPassword(con, login, pass);
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
            throw new ServiceException("unable to get the connection", throwables);
        } catch (DAOException e) {
            log.warn("unable to get user by login '{}' and pass", login, e);
            throw new ServiceException(e);
        }
        if (u != null) {
            log.debug("returned a user by login and pass {}", u);
            return u;
        }
        throw new IncorrectCredentialsException(login);
    }

    /**
     * Return a list of all users with role='user'.
     * @throws ServiceException if there are some connection issues with the db.
     */
    @Override
    public List<User> getAllWithRoleUser() throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            List<User> users = userDAO.getAllWithRoleUser(con);
            log.debug("retrieved a list of all users with role='user', list size: {}", users.size());
            return users;
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
            throw new ServiceException("unable to get the connection", throwables);
        } catch (DAOException e) {
            log.error("unable to get all users with role='user'");
            throw new ServiceException("error while downloading all users", e);
        }
    }

    @Override
    public void delete(int id) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            userDAO.remove(con, id);
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
            throw new ServiceException("unable to get the connection", throwables);
        } catch (DAOException e) {
            log.debug("unable to delete the user by id={}", id, e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateWithUserActivities(User u, List<UserActivity> activities) throws ServiceException {
        Connection con = null;
        try {
            con = dbUtils.getConnection(false);

            userDAO.update(con, u);
            log.debug("successfully updated a user");

            for (UserActivity ua : activities) {
                activityDAO.addUserActivity(con, ua);
                log.debug("successfully added a user activity {}", ua);
            }
            log.debug("successfully saved all new information about user {}", u);

            con.commit();
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
            throw new ServiceException("unable to get the connection", throwables);
        } catch (DAOException e) {
            rollback(con);
            log.error("failed to make a transaction in #updateWithUserActivities({}, {})", u, activities, e);
            throw new ServiceException("Failed to save the changes", e);
        } finally {
            close(con);
        }
    }

    private void rollback(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                log.error("failed to rollback on a connection {}", con, ex);
            }
        }
    }

    private void close(AutoCloseable con) {
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                log.warn("unable to close a resource {}", con, e);
            }
        }
    }
}
