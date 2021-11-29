package com.prusan.finalproject.db.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Abstract class that contains constants that represent queries for PreparedSet that are used for pagination with sorting.
 */
public abstract class PaginationQueries {
    private static final Logger log = LogManager.getLogger(PaginationQueries.class);

    private PaginationQueries() {}

    public static final Map<String, String> queries;
    public static final Map<String, String> names;

    static {
        queries = new HashMap<>();

        queries.put("activityAll", "SELECT * FROM activities, categories WHERE c_id = a_category_id ORDER BY %s ASC LIMIT ? OFFSET ?");
        queries.put("activity", "SELECT * FROM activities, categories WHERE c_id = a_category_id AND a_category_id IN (%s) ORDER BY %s ASC LIMIT ? OFFSET ?");
        queries.put("activityForUser", "SELECT * FROM activities, categories " +
                "WHERE a_id NOT IN (SELECT ua_activity_id FROM users_m2m_activities WHERE ua_user_id = ?) AND c_id = activities.a_category_id AND a_category_id IN (%s) ORDER BY %s ASC LIMIT ? OFFSET ?");
        queries.put("activityForUserAll", "SELECT * FROM activities, categories " +
                "WHERE a_id NOT IN (SELECT ua_activity_id FROM users_m2m_activities WHERE ua_user_id = ?) AND c_id = a_category_id ORDER BY %s ASC LIMIT ? OFFSET ?");

        names = new HashMap<>();

        names.put("activityName", Fields.ACTIVITY_NAME);
        names.put("categoryName", Fields.CATEGORY_NAME);
        names.put("usersCount", Fields.ACTIVITY_USERS_COUNT);
    }

    public static String getActivityQuery(String orderBy, String... filterBy) {
        if ("all".equals(filterBy[0])) {
            String query = String.format(queries.get("activityAll"), names.get(orderBy));
            log.debug("retrieved a query without filters: '{}'", query);
            return query;
        }
        String filters = String.join(",", filterBy);
        log.debug("got a filters string: '{}'", filters);
        String query = String.format(queries.get("activity"), filters, names.get(orderBy));
        log.debug("retrieved a query with filters: {}", query);
        return query;
    }

    public static String getActivityQueryForUser(String orderBy, String... filterBy) {
        if ("all".equals(filterBy[0])) {
            String query = String.format(queries.get("activityForUserAll"), names.get(orderBy));
            log.debug("retrieved a query without filters: {}", query);
            return query;
        }
        String filters = String.join(",", filterBy);
        log.debug("got a filters string: '{}'", filters);
        String query = String.format(queries.get("activityForUser"), filters, names.get(orderBy));
        log.debug("retrieved a query with filters: {}", query);
        return query;
    }
}
