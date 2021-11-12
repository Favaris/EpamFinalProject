package com.prusan.finalproject.db;

import com.prusan.finalproject.db.util.DBUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

public class DBUtilsTest {

    @Test
    void shouldGetDataSource() {
        assertNotNull(DBUtils.getInstance());
    }

    @Test
    void shouldGetConnection() throws SQLException {
        Connection con = DBUtils.getInstance().getConnection();
        assertNotNull(con);
    }

}
