package com.prusan.finalproject.db.dao;

import com.prusan.finalproject.db.dao.implementor.CategoryDAOImpl;
import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.util.Fields;
import com.prusan.finalproject.db.util.SQLQueries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryDAOTest {
    private ResultSet rs;
    private Statement st;
    private PreparedStatement ps;
    private Connection con;
    private CategoryDAO categoryDAO;

    @BeforeEach
    void setUp() throws Exception {
        categoryDAO = new CategoryDAOImpl();
    }

    @Test
    void addTest() throws Exception {
        rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(1);

        ps = mock(PreparedStatement.class);
        when(ps.executeUpdate()).thenReturn(1);
        when(ps.getGeneratedKeys()).thenReturn(rs);
        doThrow(new RuntimeException()).when(rs).close();

        con = mock(Connection.class);
        when(con.prepareStatement(SQLQueries.CategoryQueries.INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS))
                .thenReturn(ps);

        Category cat = new Category("test");

        categoryDAO.add(con, cat);
        assertNotNull(cat.getId());
    }

    @Test
    void getSuccessfulTest() throws Exception {
        rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(Fields.CATEGORY_ID)).thenReturn(1);
        when(rs.getString(Fields.CATEGORY_NAME)).thenReturn("test");
        doThrow(new RuntimeException()).when(rs).close();

        ps = mock(PreparedStatement.class);
        when(ps.executeQuery()).thenReturn(rs);

        con = mock(Connection.class);
        when(con.prepareStatement(SQLQueries.CategoryQueries.GET_BY_ID))
                .thenReturn(ps);

        Category cat = categoryDAO.get(con, 1);
        assertNotNull(cat);
        assertEquals("test", cat.getName());
        assertEquals(1, cat.getId());
    }

    @Test
    void getFailedTest() throws Exception {
        rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(false);

        ps = mock(PreparedStatement.class);
        when(ps.executeQuery()).thenReturn(rs);

        con = mock(Connection.class);
        when(con.prepareStatement(SQLQueries.CategoryQueries.GET_BY_ID))
                .thenReturn(ps);

        Category cat = categoryDAO.get(con, 1);
        assertNull(cat);
    }

    @Test
    void updateSuccessTest() throws Exception {
        ps = mock(PreparedStatement.class);
        when(ps.executeUpdate()).thenReturn(1);

        con = mock(Connection.class);
        when(con.prepareStatement(SQLQueries.CategoryQueries.UPDATE_CATEGORY_BY_ID))
                .thenReturn(ps);

        categoryDAO.update(con, new Category(1, "test"));
    }

    @Test
    void updateFailedTest() throws Exception {
        ps = mock(PreparedStatement.class);
        when(ps.executeUpdate()).thenReturn(0);

        con = mock(Connection.class);
        when(con.prepareStatement(SQLQueries.CategoryQueries.UPDATE_CATEGORY_BY_ID))
                .thenReturn(ps);

        categoryDAO.update(con, new Category(1, "test"));
    }

    @Test
    void getCategoriesTest() throws Exception {
        rs = mock(ResultSet.class);
        when(rs.next()).
                thenReturn(true).
                thenReturn(true).
                thenReturn(false);
        when(rs.getInt(Fields.CATEGORY_ID)).
                thenReturn(1).
                thenReturn(2);
        when(rs.getString(Fields.CATEGORY_NAME)).
                thenReturn("test1").
                thenReturn("test2");
        doThrow(new RuntimeException()).when(rs).close();

        ps = mock(PreparedStatement.class);
        when(ps.executeQuery()).thenReturn(rs);

        con = mock(Connection.class);
        when(con.prepareStatement(SQLQueries.CategoryQueries.GET_ALL_WITH_LIMIT_OFFSET))
                .thenReturn(ps);

        List<Category> categories = categoryDAO.getCategories(con, 2, 0);
        assertEquals(2, categories.size());
        assertEquals("test2", categories.get(1).getName());
    }

    @Test
    void removeSuccessTest() throws Exception {
        ps = mock(PreparedStatement.class);
        when(ps.executeUpdate()).thenReturn(1);

        con = mock(Connection.class);
        when(con.prepareStatement(SQLQueries.CategoryQueries.DELETE_CATEGORY_BY_ID))
                .thenReturn(ps);

        categoryDAO.remove(con, 1);
    }

    @Test
    void removeFailedTest() throws Exception {
        ps = mock(PreparedStatement.class);
        when(ps.executeUpdate()).thenReturn(0);

        con = mock(Connection.class);
        when(con.prepareStatement(SQLQueries.CategoryQueries.DELETE_CATEGORY_BY_ID))
                .thenReturn(ps);

        categoryDAO.remove(con, 1);
    }

    @Test
    void getCountTest() throws Exception {
        rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(10);

        st = mock(Statement.class);
        when(st.executeQuery(SQLQueries.CategoryQueries.GET_ALL_CATEGORIES_COUNT)).
                thenReturn(rs);

        con = mock(Connection.class);
        when(con.createStatement())
                .thenReturn(st);

        int count = categoryDAO.getCount(con);
        assertEquals(10, count);
    }

    @Test
    void addTestExpectedException() throws Exception {
        con = mock(Connection.class);
        when(con.prepareStatement(SQLQueries.CategoryQueries.INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS))
                .thenThrow(new SQLException());

        Category cat = new Category("test");

        assertThrows(DAOException.class, () -> categoryDAO.add(con, cat));
    }

    @Test
    void getTestExpectedException() throws Exception {
        con = mock(Connection.class);
        when(con.prepareStatement(SQLQueries.CategoryQueries.GET_BY_ID))
                .thenThrow(new SQLException());

        assertThrows(DAOException.class, () -> categoryDAO.get(con, 1));
    }

    @Test
    void updateTestExpectedException() throws Exception {
        con = mock(Connection.class);
        when(con.prepareStatement(SQLQueries.CategoryQueries.UPDATE_CATEGORY_BY_ID))
                .thenThrow(new SQLException());

        assertThrows(DAOException.class, () -> categoryDAO.update(con, new Category(1, "test")));
    }

    @Test
    void removeTestExpectedException() throws Exception {
        con = mock(Connection.class);
        when(con.prepareStatement(SQLQueries.CategoryQueries.DELETE_CATEGORY_BY_ID))
                .thenThrow(new SQLException());

        assertThrows(DAOException.class, () -> categoryDAO.remove(con, 1));
    }

    @Test
    void getCategoriesTestExpectedException() throws Exception {
        con = mock(Connection.class);
        when(con.prepareStatement(SQLQueries.CategoryQueries.GET_ALL_WITH_LIMIT_OFFSET))
                .thenThrow(new SQLException());

        assertThrows(DAOException.class, () -> categoryDAO.getCategories(con, 2, 0));
    }

    @Test
    void getCountTestExpectedException() throws Exception {
        con = mock(Connection.class);
        when(con.createStatement())
                .thenThrow(new SQLException());

        assertThrows(DAOException.class, () -> categoryDAO.getCount(con));
    }
}
