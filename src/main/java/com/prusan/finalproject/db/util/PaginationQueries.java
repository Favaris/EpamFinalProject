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

        queries.put("activityAll", "SELECT * FROM activities, categories WHERE c_id = a_category_id ORDER BY %s DESC LIMIT ? OFFSET ?");
        queries.put("activity", "SELECT * FROM activities, categories WHERE c_id = a_category_id AND a_category_id IN (%s) ORDER BY %s DESC LIMIT ? OFFSET ?");

        queries.put("activityForUserAll", "SELECT * FROM activities, categories " +
                "WHERE a_id NOT IN (SELECT ua_activity_id FROM users_m2m_activities WHERE ua_user_id = ?) AND c_id = a_category_id ORDER BY %s DESC LIMIT ? OFFSET ?");
        queries.put("activityForUser", "SELECT * FROM activities, categories " +
                "WHERE a_id NOT IN (SELECT ua_activity_id FROM users_m2m_activities WHERE ua_user_id = ?) AND c_id = activities.a_category_id AND a_category_id IN (%s) ORDER BY %s DESC LIMIT ? OFFSET ?");

        queries.put("userActivityAll", "SELECT * FROM activities " +
                "JOIN users_m2m_activities ON a_id = ua_activity_id AND ua_accepted = true AND ua_requested_abandon = false AND ua_user_id = ? " +
                "JOIN categories ON c_id = a_category_id ORDER BY %s DESC LIMIT ? OFFSET ?");
        queries.put("userActivity", "SELECT * FROM activities " +
                "JOIN users_m2m_activities ON a_id = ua_activity_id AND ua_accepted = true AND ua_requested_abandon = false AND ua_user_id = ? " +
                "JOIN categories ON c_id = a_category_id AND c_id IN (%s) ORDER BY %s DESC LIMIT ? OFFSET ?");

        queries.put("users", "SELECT * FROM users " +
                "JOIN user_infos ui ON u_id = ui_user_id AND u_login LIKE '%%%s%%' %s " +
                "ORDER BY %s DESC LIMIT ? OFFSET ?");

        names = new HashMap<>();

        names.put("activityName", Fields.ACTIVITY_NAME);
        names.put("categoryName", Fields.CATEGORY_NAME);
        names.put("usersCount", Fields.ACTIVITY_USERS_COUNT);
        names.put("timeSpent", Fields.USER_ACTIVITY_MINUTES_SPENT);

        names.put("userLogin", Fields.USER_LOGIN);
        names.put("activitiesCount", Fields.USER_INFOS_ACTIVITIES_COUNT);
        names.put("totalTime", Fields.USER_INFOS_TOTAL_TIME);
    }

    public static String getActivityQuery(String orderBy, String... filterBy) {
        if ("all".equals(filterBy[0])) {
            String query = String.format(queries.get("activityAll"), names.get(orderBy));
            log.debug("retrieved a query without filters: '{}'", query);
            return query;
        }
        return getQueryWithFilters(orderBy, filterBy, "activity");
    }

    public static String getActivityQueryForUser(String orderBy, String... filterBy) {
        if ("all".equals(filterBy[0])) {
            String query = String.format(queries.get("activityForUserAll"), names.get(orderBy));
            log.debug("retrieved a query without filters: '{}'", query);
            return query;
        }
        return getQueryWithFilters(orderBy, filterBy, "activityForUser");
    }

    public static String getUserActivityQuery(String orderBy, String... filterBy) {
        if ("all".equals(filterBy[0])) {
            String query = String.format(queries.get("userActivityAll"), names.get(orderBy));
            log.debug("retrieved a query without filters: '{}'", query);
            return query;
        }
        return getQueryWithFilters(orderBy, filterBy, "userActivity");
    }

    public static String getUserQuery(String orderBy, String countLessThen, String countBiggerThen, String like) {
        StringBuilder restrictionQueryBuilder = new StringBuilder();

        if (!countLessThen.isEmpty()) {
            String lessThenQuery = String.format("%s < %s", Fields.USER_INFOS_ACTIVITIES_COUNT, countLessThen);
            log.debug("generated a less then restriction: '{}'", lessThenQuery);
            restrictionQueryBuilder.append("AND ").append(lessThenQuery);
        }

        if (!countBiggerThen.isEmpty()) {
            String biggerThenQuery = String.format("%s > %s", Fields.USER_INFOS_ACTIVITIES_COUNT, countBiggerThen);
            log.debug("generated a bigger then restriction: '{}'", biggerThenQuery);
            restrictionQueryBuilder.append(" AND ").append(biggerThenQuery);
        }

        String restrictionQuery = restrictionQueryBuilder.toString();
        log.debug("generated a restriction query: '{}'", restrictionQuery);

        String query = String.format(queries.get("users"), like, restrictionQuery, names.get(orderBy));
        log.debug("generated a query: '{}'", query);
        return query;
    }

    private static String getQueryWithFilters(String orderBy, String[] filterBy, String queryName) {
        String filters = String.join(",", filterBy);
        log.debug("got a filters string: '{}'", filters);
        String query = String.format(queries.get(queryName), filters, names.get(orderBy));
        log.debug("retrieved a query with filters: '{}'", query);
        return query;
    }

}
