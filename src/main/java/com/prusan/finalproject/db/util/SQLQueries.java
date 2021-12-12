package com.prusan.finalproject.db.util;

/**
 * Abstract class that contains interfaces with SQL queries as constant Strings.
 */
public abstract class SQLQueries {
    private SQLQueries() {}

    public interface ActivityQueries {

        String INSERT_ACTIVITY = "INSERT INTO activities(a_name, a_description, a_category_id) VALUES (?,?,?)";
        String GET_ACTIVITY_BY_ID = "SELECT * FROM activities, categories WHERE a_id = ?";
        String UPDATE_ACTIVITY_BY_ID = "UPDATE activities SET a_name = ?, a_description = ?, a_category_id = ? WHERE a_id = ?";
        String DELETE_ACTIVITY_BY_ID = "DELETE FROM activities WHERE a_id = ?";

        String GET_COUNT_OF_AVAILABLE_ACTIVITIES_FOR_USER = "SELECT COUNT(*) FROM activities WHERE a_id NOT IN (SELECT ua_activity_id FROM users_m2m_activities WHERE ua_user_id = ?)";
        String GET_AVAILABLE_ACTIVITIES_COUNT_WITH_FILTERS = "SELECT COUNT(*) FROM activities WHERE a_id NOT IN (SELECT ua_activity_id FROM users_m2m_activities WHERE ua_user_id = ?) AND a_category_id IN (%s)";
    }

    public interface CategoryQueries {

        String INSERT_CATEGORY = "INSERT INTO categories(c_name) VALUES (?)";
        String UPDATE_CATEGORY_BY_ID = "UPDATE categories SET c_name = ? WHERE c_id = ?";
        String DELETE_CATEGORY_BY_ID = "DELETE FROM categories WHERE c_id = ?";
        String GET_BY_ID = "SELECT * FROM categories WHERE c_id = ?";

        String GET_ALL_WITH_LIMIT_OFFSET = "SELECT * FROM categories LIMIT ? OFFSET ?";
    }

    public interface UserActivityQueries {

        String INSERT_USER_ACTIVITY = "INSERT INTO users_m2m_activities VALUES (?, ?, ?, ?, ?)";
        String GET_REQUESTED_USERS_ACTIVITIES =
                "SELECT * FROM users_m2m_activities, activities, categories WHERE (ua_accepted = 0 OR ua_requested_abandon = 1) AND a_id = ua_activity_id AND c_id = a_category_id %s LIMIT ? OFFSET ?";
        String GET_REQUESTS_COUNT = "SELECT COUNT(*) FROM users_m2m_activities WHERE (ua_accepted = false OR ua_requested_abandon = true) %s";
        String UPDATE_USER_ACTIVITY = "UPDATE users_m2m_activities SET ua_accepted = ?, ua_minutes_spent = ?, ua_requested_abandon = ? WHERE ua_activity_id = ? AND ua_user_id = ?";
        String DELETE_USER_ACTIVITY = "DELETE FROM users_m2m_activities WHERE ua_user_id = ? AND ua_activity_id = ?";
        String GET_USER_ACTIVITY =
                "SELECT * FROM users_m2m_activities, activities, categories WHERE ua_user_id = ? AND ua_activity_id = ? AND ua_activity_id = a_id AND a_category_id = c_id";
        String GET_ALL_COUNT_BY_USER_ID = "SELECT COUNT(*) FROM users_m2m_activities WHERE ua_user_id = ? AND ua_accepted = true";
        String GET_FILTERED_COUNT_BY_USER_ID = "SELECT COUNT(*) FROM users_m2m_activities, activities WHERE ua_user_id = ? AND ua_activity_id = a_id AND a_category_id IN (%s)";

    }

    public interface UserQueries {

        String INSERT_USER = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?, ?, ?)";
        String GET_USER_BY_ID = "SELECT * FROM users WHERE u_id = ?";
        String UPDATE_USER_BY_ID = "UPDATE users SET u_login = ?, u_password = ?, u_name = ?, u_surname = ? WHERE u_id = ?";
        String DELETE_USER_BY_ID = "DELETE FROM users WHERE u_id = ?";
        String GET_USER_INFO_BY_ID = "SELECT * FROM user_infos WHERE ui_user_id = ?";
        String SELECT_USER_BY_LOGIN = "SELECT * FROM users WHERE u_login = ?";
        String GET_COUNT_WITH_ROLE_USER = "SELECT COUNT(*) FROM users JOIN user_infos ON u_id = ui_user_id AND u_role = 'user' AND u_login LIKE '%%%s%%' %s";
    }
}
