package com.prusan.finalproject.db.dao.implementor;

import com.prusan.finalproject.db.dao.CategoryDAO;
import com.prusan.finalproject.db.dao.DAOException;
import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.util.Fields;
import com.prusan.finalproject.db.util.SQLQueries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    /**
     * Add a new category to the db. If operation was successful, updated id field on given Category object.
     * @throws DAOException if such category is already in the db or some exceptions occurred with statements/resultsets processing.
     */
    @Override
    public void add(Connection con, Category category) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(SQLQueries.CategoryQueries.INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS)) {
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
     *
     * @param id of category
     * @return Category obj found by given id or null if no object was found.
     * @throws DAOException only if statement or result set throw an ex.
     */
    @Override
    public Category get(Connection con, int id) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(SQLQueries.CategoryQueries.GET_BY_ID)) {
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
        try (PreparedStatement ps = con.prepareStatement(SQLQueries.CategoryQueries.UPDATE_CATEGORY_BY_ID)) {
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
        try (PreparedStatement ps = con.prepareStatement(SQLQueries.CategoryQueries.DELETE_CATEGORY_BY_ID)) {
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

    @Override
    public List<Category> getCategories(Connection con, int limit, int offset) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(SQLQueries.CategoryQueries.GET_ALL_WITH_LIMIT_OFFSET)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            rs = ps.executeQuery();
            List<Category> categories = new ArrayList<>();
            while (rs.next()) {
                Category category = getCategory(rs);
                categories.add(category);
                log.debug("added a category {} to the list", category);
            }
            log.debug("received a list of categories, list size: {}", categories.size());
            return categories;
        } catch (SQLException throwables) {
            log.error("failed to retrieve all categories with limit={}, offset={}", limit, offset, throwables);
            throw new DAOException("failed to get categories", throwables);
        }
    }

    @Override
    public int getCount(Connection con) throws DAOException {
        try (Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM categories")) {
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            log.debug("retrieved a categories' count: {}", count);
            return count;
        } catch (SQLException throwables) {
            log.error("failed to get a categories' count", throwables);
            throw new DAOException("failed to get a categories' count", throwables);
        }
    }

    private Category getCategory(ResultSet rs) throws SQLException {
        Category ct = new Category();
        ct.setId(rs.getInt(Fields.CATEGORY_ID));
        ct.setName(rs.getString(Fields.CATEGORY_NAME));
        log.debug("retrieved a category {}", ct);
        return ct;
    }
}
