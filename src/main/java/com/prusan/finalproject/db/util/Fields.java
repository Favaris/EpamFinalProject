package com.prusan.finalproject.db.util;

/**
 * Abstract class that contains names of tables' attributes in the db.
 */
public abstract class Fields {
    private Fields() {}

    public static final String USER_ID = "u_id";
    public static final String USER_LOGIN = "u_login";
    public static final String USER_PASSWORD = "u_password";
    public static final String USER_NAME = "u_name";
    public static final String USER_SURNAME = "u_surname";
    public static final String USER_ROLE = "u_role";

    public static final String USER_INFOS_USER_ID = "ui_user_id";
    public static final String USER_INFOS_ACTIVITIES_COUNT = "ui_activities_count";
    public static final String USER_INFOS_TOTAL_TIME = "ui_total_time";

    public static final String CATEGORY_ID = "c_id";
    public static final String CATEGORY_NAME = "c_name";

    public static final String ACTIVITY_ID = "a_id";
    public static final String ACTIVITY_CATEGORY_ID = "a_category_id";
    public static final String ACTIVITY_NAME = "a_name";
    public static final String ACTIVITY_DESCRIPTION = "a_description";
    public static final String ACTIVITY_USERS_COUNT = "a_users_count";

    public static final String USER_ACTIVITY_USER_ID = "ua_user_id";
    public static final String USER_ACTIVITY_ACTIVITY_ID = "ua_activity_id";
    public static final String USER_ACTIVITY_ACCEPTED = "ua_accepted";
    public static final String USER_ACTIVITY_MINUTES_SPENT = "ua_minutes_spent";
    public static final String USER_ACTIVITY_REQUESTED_ABANDON = "ua_requested_abandon";
}
