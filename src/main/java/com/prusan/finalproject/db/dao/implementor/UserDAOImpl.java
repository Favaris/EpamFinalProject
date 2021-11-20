package com.prusan.finalproject.db.dao.implementor;

import com.prusan.finalproject.db.dao.DAOException;
import com.prusan.finalproject.db.dao.UserDAO;
import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.entity.UserActivity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementor class for UserDAO. Has SQL queries as private static fields.
 */
public class UserDAOImpl extends UserDAO {
    private static final Logger log = LogManager.getLogger(UserDAOImpl.class);

    private static final String INSERT_USER = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?, ?, ?)";
    private static final String GET_USER_BY_ID = "SELECT * FROM users u WHERE u.id = ?";
    private static final String UPDATE_USER_BY_ID = "UPDATE users u SET login = ?, password = ?, name = ?, surname = ? WHERE u.id = ?";
    private static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    public static final String SELECT_USER_BY_LOGIN = "SELECT * FROM users u WHERE u.login = ?";
    public static final String GET_ALL_ACCEPTED_ACTIVITIES_BY_ID = "SELECT * FROM users_m2m_activities ua, activities a WHERE ua.user_id = ? AND accepted = 1 AND requested_abandon = 0 AND a.id = ua.activity_id";

    /**
     * Inserts a user with the given fields. For passed user object, updates the id field if the insertion was successful.
     * @param user user to add
     * @throws DAOException thrown if the given user can not be inserted.
     */
    @Override
    public void add(Connection con, User user) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS))
        {
            int k = 0;
            ps.setString(++k, user.getLogin());
            ps.setString(++k, user.getPassword());
            ps.setString(++k, user.getName());
            ps.setString(++k, user.getSurname());
            ps.setString(++k, user.getRole());

            if (ps.executeUpdate() > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                    log.debug("inserted a user: {}", user);
                }
            }
        } catch (SQLException throwables) {
            log.warn("exception in #add(user={})", user, throwables);
            throw new DAOException("can not insert user", throwables);
        } finally {
            try {
                close(rs);
            } catch (DAOException ex) {
                log.warn("unable to close the result set", ex);
            }
        }
    }

    /**
     * Gets user by its id.
     * @param con data source connection.
     * @param id user id.
     * @return null if user was not found and user object otherwise.
     * @throws DAOException when some exceptions were caught.
     */
    @Override
    public User get(Connection con, int id) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(GET_USER_BY_ID))
        {
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                User u = getUser(rs);
                log.debug("returned a user from db: {}", u);
                return u;
            }
        } catch (SQLException throwables) {
            log.debug("unable to get user by id={}", id, throwables);
            throw new DAOException("unable to retrieve a user by id=" + id, throwables);
        } finally {
            try {
                close(rs);
            } catch (DAOException ex) {
                log.warn("unable to close result set in method get(id), ", ex);
            }
        }

        return null;
    }

    /**
     * Updates all fields (except for id) of the given user in db.
     * @param user a user to update
     * @throws DAOException is thrown if the given user can not be updated by any reason.
     */
    @Override
    public void update(Connection con, User user) throws DAOException {
        try (PreparedStatement ps = con.prepareStatement(UPDATE_USER_BY_ID))
        {
            int k = 0;
            ps.setString(++k, user.getLogin());
            ps.setString(++k, user.getPassword());
            ps.setString(++k, user.getName());
            ps.setString(++k, user.getSurname());
            ps.setInt(++k, user.getId());

            if (ps.executeUpdate() > 0) {
                log.debug("updated a user {}", user);
            } else {
                log.debug("unable to update a user {}", user);
            }
        } catch (SQLException throwables) {
            log.warn("unable to update a user {} by id", user, throwables);
            throw new DAOException("can not update user", throwables);
        }
    }

    @Override
    public void remove(Connection con, int id) throws DAOException {
        try (PreparedStatement ps = con.prepareStatement(DELETE_USER_BY_ID))
        {
            ps.setInt(1, id);
            ps.executeUpdate();
            log.debug("removed a user #{} from the db", id);
        } catch (SQLException ex) {
            log.warn("exception in #remove(id={}) ", id, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<User> getAll(Connection con) throws DAOException {
        List<User> users = new ArrayList<>();
        try (Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(SELECT_ALL_USERS))
        {
            while (rs.next()) {
                User u = getUser(rs);
                users.add(u);
                log.debug("extracted a user: {}", u);
            }
        } catch (SQLException throwables) {
            log.warn("unable to extract all users ", throwables);
            throw new DAOException("unable to extract all users", throwables);
        }
        return users;
    }

    @Override
    public User getByLogin(Connection con, String login) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(SELECT_USER_BY_LOGIN)){
            ps.setString(1, login);
            rs = ps.executeQuery();
            if (rs.next()) {
                User u = getUser(rs);
                log.debug("extracted user by login: {}", u);
                return u;
            }
        } catch (SQLException throwables) {
            log.warn("exception in #getByLogin(login={})", login, throwables);
            throw new DAOException(throwables);
        }
        log.debug("unable to find user by login {}:", login);
        return null;
    }

    /**
     * Extracts a user by login only and then compares its password with the given one.
     */
    @Override
    public User getByLoginAndPassword(Connection con, String login, String password) throws DAOException {
        User u = getByLogin(con, login);
        if (u != null && u.getPassword().equals(password)) {
            log.debug("extracted user by login and pass: {}", u);
            return u;
        }
        log.debug("incorrect password for user {}", login);

        return null;
    }

    /**
     * Retrieves a list of all user's activities that are accepted and not requested for abandonment.
     */
    @Override
    public List<UserActivity> getRunningActivities(Connection con, int id) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(GET_ALL_ACCEPTED_ACTIVITIES_BY_ID)) {
            ps.setInt(1, id);

            rs = ps.executeQuery();
            List<UserActivity> uas = new ArrayList<>();
            while (rs.next()) {
                UserActivity ua = ActivityDAOImpl.getUserActivity(rs);
                log.debug("retrieved accepted user activity {}", ua);
                uas.add(ua);
            }
            log.debug("retrieved a list of all accepted user activities, list size: {}", uas.size());
            return uas;
        } catch (SQLException throwables) {
            log.error("error in getRunningActivities(id={})", id, throwables);
            throw new DAOException(throwables);
        }
    }

    private User getUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setLogin(rs.getString("login"));
        u.setPassword(rs.getString("password"));
        u.setName(rs.getString("name"));
        u.setSurname(rs.getString("surname"));
        u.setRole(rs.getString("role"));
        return u;
    }


}
