package com.prusan.finalproject.db.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class that contains constants that represent queries for PreparedSet that are used for pagination with sorting.
 */
public abstract class PaginationQueries {
    private static final Logger log = LogManager.getLogger(PaginationQueries.class);

    private PaginationQueries() {}

    public static final Map<String, String> queries;

    static {
        queries = new HashMap<>();

        queries.put("name", "SELECT * FROM activities ORDER BY name ASC LIMIT ? OFFSET ?");
        queries.put("userAmount", "SELECT * FROM activities ORDER BY users_count ASC LIMIT ? OFFSET ?");
        queries.put("category", "SELECT * " +
                "FROM ( " +
                "     SELECT a.id, " +
                "            a.name, " +
                "            c.name catname, " +
                "            a.description, " +
                "            a.users_count, " +
                "            ROW_NUMBER() over (PARTITION BY a.name) rn " +
                "     FROM activities a, categories c, categories_m2m_activities ca " +
                "     WHERE ca.category_id = c.id AND ca.activity_id = a.id " +
                "         ) res " +
                "WHERE rn = 1 " +
                "ORDER BY catname ASC LIMIT ? OFFSET ?");
    }

    public static String getQuery(String orderBy) {
        String res = queries.get(orderBy);
        log.debug("returned a query for {}, query: {}", orderBy, res);
        return res;
    }
}
