package com.prusan.finalproject.db.util;

/**
 * Interface that contains classes with SQL queries as constant Strings.
 */
public interface SQLQueries {
    abstract class ActivityQueries {
        private ActivityQueries() {}

        public static final String INSERT_ACTIVITY = "INSERT INTO activities(a_name, a_description, a_category_id) VALUES (?,?,?)";
        public static final String GET_ACTIVITY_BY_ID = "SELECT * FROM activities, categories WHERE a_id = ?";
        public static final String UPDATE_ACTIVITY_BY_ID = "UPDATE activities SET a_name = ?, a_description = ?, a_category_id = ? WHERE a_id = ?";
        public static final String DELETE_ACTIVITY_BY_ID = "DELETE FROM activities WHERE a_id = ?";
        public static final String GET_ALL_ACTIVITIES = "SELECT * FROM activities, categories WHERE a_category_id = c_id";

        public static final String GET_COUNT_OF_AVAILABLE_ACTIVITIES_FOR_USER = "SELECT COUNT(*) FROM activities WHERE a_id NOT IN (SELECT ua_activity_id FROM users_m2m_activities WHERE ua_user_id = ?)";
        public static final String GET_AVAILABLE_ACTIVITIES_COUNT_WITH_FILTERS = "SELECT COUNT(*) FROM activities WHERE a_id NOT IN (SELECT ua_activity_id FROM users_m2m_activities WHERE ua_user_id = ?) AND a_category_id IN (%s)";
    }

    abstract class CategoryQueries {
        private CategoryQueries() {}

        public static final String GET_ALL_CATEGORIES = "SELECT * FROM categories";
        public static final String INSERT_CATEGORY = "INSERT INTO categories(c_name) VALUES (?)";
        public static final String UPDATE_CATEGORY_BY_ID = "UPDATE categories SET c_name = ? WHERE c_id = ?";
        public static final String DELETE_CATEGORY_BY_ID = "DELETE FROM categories WHERE c_id = ?";
        public static final String GET_BY_ID = "SELECT * FROM categories WHERE c_id = ?";

        public static final String GET_ALL_WITH_LIMIT_OFFSET = "SELECT * FROM categories LIMIT ? OFFSET ?";
    }

    abstract class UserActivityQueries {
        private UserActivityQueries() {}

        public static final String GET_ALL_ACCEPTED_ACTIVITIES_BY_ID = "SELECT * FROM users_m2m_activities, activities, categories WHERE ua_user_id = ? AND ua_accepted = 1 AND ua_requested_abandon = 0 AND a_id = ua_activity_id AND c_id = a_category_id";
        public static final String INSERT_USER_ACTIVITY = "INSERT INTO users_m2m_activities VALUES (?, ?, ?, ?, ?)";
        public static final String GET_REQUESTED_USERS_ACTIVITIES =
                "SELECT * FROM users_m2m_activities, activities, categories WHERE (ua_accepted = 0 OR ua_requested_abandon = 1) AND a_id = ua_activity_id AND c_id = a_category_id %s LIMIT ? OFFSET ?";
        public static final String GET_REQUESTS_COUNT = "SELECT COUNT(*) FROM users_m2m_activities WHERE (ua_accepted = false OR ua_requested_abandon = true) %s";
        public static final String UPDATE_USER_ACTIVITY = "UPDATE users_m2m_activities SET ua_accepted = ?, ua_minutes_spent = ?, ua_requested_abandon = ? WHERE ua_activity_id = ? AND ua_user_id = ?";
        public static final String DELETE_USER_ACTIVITY = "DELETE FROM users_m2m_activities WHERE ua_user_id = ? AND ua_activity_id = ?";
        public static final String GET_USER_ACTIVITY =
                "SELECT * FROM users_m2m_activities, activities, categories WHERE ua_user_id = ? AND ua_activity_id = ? AND ua_activity_id = a_id AND a_category_id = c_id";
        public static final String DELETE_ALL_BY_USER_ID = "DELETE FROM users_m2m_activities WHERE ua_user_id = ?";
        public static final String GET_ALL_COUNT_BY_USER_ID = "SELECT COUNT(*) FROM users_m2m_activities WHERE ua_user_id = ? AND ua_accepted = true";
        public static final String GET_FILTERED_COUNT_BY_USER_ID = "SELECT COUNT(*) FROM users_m2m_activities, activities WHERE ua_user_id = ? AND ua_activity_id = a_id AND a_category_id IN (%s)";

        public static final String GET_SUM_OF_MINUTES_BY_USER_ID = "SELECT SUM(ua_minutes_spent) FROM users_m2m_activities WHERE ua_user_id = ?";
    }

    abstract class UserQueries {
        private UserQueries() {
        }

        public static final String INSERT_USER = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?, ?, ?)";
        public static final String GET_USER_BY_ID = "SELECT * FROM users WHERE u_id = ?";
        public static final String UPDATE_USER_BY_ID = "UPDATE users SET u_login = ?, u_password = ?, u_name = ?, u_surname = ? WHERE u_id = ?";
        public static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE u_id = ?";
        public static final String SELECT_ALL_USERS = "SELECT * FROM users";
        public static final String GET_USER_INFO_BY_ID = "SELECT * FROM user_infos WHERE ui_user_id = ?";
        public static final String SELECT_USER_BY_LOGIN = "SELECT * FROM users WHERE u_login = ?";
        public static final String GET_DEFAULT_USERS = "SELECT * FROM users WHERE u_role='user' LIMIT ? OFFSET ?";

        public static final String GET_COUNT_WITH_ROLE_USER = "SELECT COUNT(*) FROM users JOIN user_infos ON u_id = ui_user_id AND u_role = 'user' AND u_login LIKE '%%%s%%' %s";
    }
}
