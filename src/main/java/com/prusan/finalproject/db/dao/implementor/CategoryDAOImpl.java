package com.prusan.finalproject.db.dao.implementor;

import com.prusan.finalproject.db.dao.CategoryDAO;
import com.prusan.finalproject.db.dao.DAOException;
import com.prusan.finalproject.db.entity.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl extends CategoryDAO {
    private static final Logger log = LogManager.getLogger(CategoryDAOImpl.class);
    public static final String GET_ALL_CATEGORIES = "SELECT * FROM categories";
    public static final String INSERT_CATEGORY = "INSERT INTO categories(name) VALUES (?)";
    public static final String UPDATE_CATEGORY_BY_ID = "UPDATE categories SET name = ? WHERE id = ?";
    public static final String DELETE_CATEGORY_BY_ID = "DELETE FROM categories WHERE id = ?";
    public static final String GET_CATEGORY_BY_NAME = "SELECT * FROM categories WHERE name = ?";

    /**
     * Add a new category to the db. If operation was successful, updated id field on given Category object.
     * @throws DAOException if such category is already in the db or some exceptions occurred with statements/resultsets processing.
     */
    @Override
    public void add(Connection con, Category category) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getName());

            if (ps.executeUpdate() > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()){
                    log.debug("inserted a new category {}", category);
                    category.setId(rs.getInt(1));
                }
            }
        } catch (SQLException throwables) {
            log.debug("unable to insert a category {}", category, throwables);
            log.warn("exception in #add({})", category, throwables);
            throw new DAOException(throwables);
        } finally {
            try {
                close(rs);
            } catch (DAOException ex) {
                log.warn("unable to close a resource {}", rs, ex);
            }
        }
    }

    /**
     * Returns all categories in the db.
     * @return a list of all categories in the db.
     * @throws DAOException only if statement or result set throw an ex.
     */
    @Override
    public List<Category> getAll(Connection con) throws DAOException {
        List<Category> categories = new ArrayList<>();
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(GET_ALL_CATEGORIES)) {
            while (rs.next()) {
                Category ct = getCategory(rs);
                categories.add(ct);
            }
        } catch (SQLException throwables) {
            log.warn("exception in #getAll()", throwables);
            throw new DAOException(throwables);
        }
        return categories;
    }

    /**
     *
     * @param id of category
     * @return Category obj found by given id or null if no object was found.
     * @throws DAOException only if statement or result set throw an ex.
     */
    @Override
    public Category get(Connection con, int id) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM categories WHERE id = ?")) {
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return getCategory(rs);
            }
        } catch (SQLException throwables) {
            log.warn("exception in #get(id={})", id, throwables);
            throw new DAOException(throwables);
        } finally {
            try {
                close(rs);
            } catch (DAOException e) {
                log.warn("unable to close resource {}", rs, e);
            }
        }
        return null;
    }

    /**
     * Updates category corresponding to given object. Does not update the id field.
     * @param con connection to db
     * @param category activity to be updated
     * @throws DAOException if caught some exceptions on connection/statement lifecycle.
     */
    @Override
    public void update(Connection con, Category category) throws DAOException {
        try (PreparedStatement ps = con.prepareStatement(UPDATE_CATEGORY_BY_ID)) {
            int k = 0;
            ps.setString(++k, category.getName());
            ps.setInt(++k, category.getId());

            if (ps.executeUpdate() > 0) {
                log.debug("updated a category {}", category);
            } else {
                log.debug("unable to update a category {}", category);
            }
        } catch (SQLException throwables) {
            log.warn("exception in #update({})", category, throwables);
            throw new DAOException(throwables);
        }
    }

    @Override
    public void remove(Connection con, int id) throws DAOException {
        try (PreparedStatement ps = con.prepareStatement(DELETE_CATEGORY_BY_ID)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                log.debug("deleted a category #id={}", id);
            } else {
                log.debug("unable to delete a category #id={}", id);
            }
        } catch (SQLException throwables) {
            log.warn("exception in #remove(id={})", id, throwables);
            throw new DAOException(throwables);
        }
    }

    /**
     * Gets a category by its name.
     * @return Category obj if such category was found, null otherwise.
     * @throws DAOException only if statement logic was wrong, or no connection with the db.
     */
    @Override
    public Category getByName(Connection con, String name) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(GET_CATEGORY_BY_NAME)) {
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                Category ct = new Category();
                ct.setId(rs.getInt("id"));
                ct.setName(rs.getString("name"));
                log.debug("retrieved a category by name: {}", ct);
                return ct;
            }
        } catch (SQLException throwables) {
            log.warn("exception in #getByName(name={})", name, throwables);
            throw new DAOException(throwables);
        }
        log.debug("unable to find a category by name {}", name);
        return null;
    }

    private Category getCategory(ResultSet rs) throws SQLException {
        Category ct = new Category();
        ct.setId(rs.getInt("id"));
        ct.setName(rs.getString("name"));
        log.debug("retrieved a category {}", ct);
        return ct;
    }
}
