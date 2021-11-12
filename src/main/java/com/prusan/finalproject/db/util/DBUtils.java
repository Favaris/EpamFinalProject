package com.prusan.finalproject.db.util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class for getting the connection between database and application.
 * Uses DataSource, which is configured in context.xml file. This class is using singleton pattern.
 */
public class DBUtils {
    private static final Logger log = LogManager.getLogger(DBUtils.class);
    private final DataSource ds;
    // singleton instance
    private static DBUtils instance;

    public static synchronized DBUtils getInstance() {
        if (instance == null) {
            try {
                instance = new DBUtils();
            } catch (NamingException e) {
                log.fatal("can not obtain a data source, {}", e.getMessage());
            }
        }
        return instance;
    }

    // getting DataSource with the creating of an instance.
    private DBUtils() throws NamingException {
        Context initContext = new InitialContext();
        Context envContext  = (Context) initContext.lookup("java:/comp/env");
        ds = (DataSource) envContext.lookup("jdbc/time_accounting_db");
    }

    public Connection getConnection(boolean autoCommit) throws SQLException {
        Connection con = ds.getConnection();
        con.setAutoCommit(autoCommit);
        log.debug("opened a connection {}", con);
        return con;
    }

    public Connection getConnection() throws SQLException {
        return getConnection(true);
    }
}
