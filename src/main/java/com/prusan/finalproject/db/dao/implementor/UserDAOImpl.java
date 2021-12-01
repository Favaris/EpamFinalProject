package com.prusan.finalproject.db.dao.implementor;

import com.prusan.finalproject.db.dao.DAOException;
import com.prusan.finalproject.db.dao.UserDAO;
import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.util.Fields;
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
    private static final String GET_USER_BY_ID = "SELECT * FROM users WHERE u_id = ?";
    private static final String UPDATE_USER_BY_ID = "UPDATE users SET u_login = ?, u_password = ?, u_name = ?, u_surname = ? WHERE u_id = ?";
    private static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE u_id = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    public static final String SELECT_USER_BY_LOGIN = "SELECT * FROM users WHERE u_login = ?";
    public static final String GET_ALL_ADMINS = "SELECT * FROM users WHERE u_role='admin'";
    public static final String GET_ALL_WITH_ROLE_USER = "SELECT * FROM users WHERE u_role='user'";

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
     * Returns a list of users with role='admin'
     * @throws DAOException if there are some issues with the connection to the db.
     */
    @Override
    public List<User> getAllAdmins(Connection con) throws DAOException {
        List<User> admins = new ArrayList<>();
        try (Statement ps = con.createStatement();
            ResultSet rs = ps.executeQuery(GET_ALL_ADMINS)) {
            while (rs.next()) {
                User u = getUser(rs);
                log.debug("retrieved an admin user: {}", u);
                admins.add(u);
            }
            log.debug("got a list of all admins, list size: {}", admins.size());
        } catch (SQLException throwables) {
            log.error("error in getAllAdmins()", throwables);
            throw new DAOException(throwables);
        }
        return admins;
    }

    /**
     * Returns a list of users with role='user'.
     * @throws DAOException if there are some issues with the connection to the db.
     */
    @Override
    public List<User> getAllWithRoleUser(Connection con) throws DAOException {
        List<User> users = new ArrayList<>();
        try (Statement ps = con.createStatement();
             ResultSet rs = ps.executeQuery(GET_ALL_WITH_ROLE_USER)) {
            while (rs.next()) {
                User u = getUser(rs);
                log.debug("retrieved a user with role='user': {}", u);
                users.add(u);
            }
            log.debug("got a list of all users with role='user', list size: {}", users.size());
        } catch (SQLException throwables) {
            log.error("error in getAllWithRoleUser()", throwables);
            throw new DAOException(throwables);
        }
        return users;
    }

    private User getUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt(Fields.USER_ID));
        u.setLogin(rs.getString(Fields.USER_LOGIN));
        u.setPassword(rs.getString(Fields.USER_PASSWORD));
        u.setName(rs.getString(Fields.USER_NAME));
        u.setSurname(rs.getString(Fields.USER_SURNAME));
        u.setRole(rs.getString(Fields.USER_ROLE));
        return u;
    }
}
