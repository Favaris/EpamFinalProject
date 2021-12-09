package com.prusan.finalproject.web.constant;

/**
 * Abstract class with static final fields for relative path to the pages.
 */
public abstract class Pages {
    private Pages() {}

    public static final String SIGN_IN_JSP = "jsp/signIn.jsp";
    public static final String SIGN_UP_JSP = "jsp/signUp.jsp";
    public static final String ERROR_JSP = "jsp/error.jsp";

    public static final String HOME_JSP = "jsp/logged/home.jsp";
    public static final String ACTIVITIES_JSP = "jsp/logged/activities.jsp";
    public static final String REQUESTS_JSP = "jsp/logged/requests.jsp";

    public static final String RUNNING_ACTIVITIES_JSP = "jsp/logged/user/runningActivities.jsp";

    public static final String USERS_JSP = "jsp/logged/admin/users.jsp";
    public static final String ACTIVITY_EDIT_PAGE_JSP = "jsp/logged/admin/util/activityEditPage.jsp";
    public static final String ACTIVITY_ADD_PAGE_JSP = "jsp/logged/admin/util/activityAddPage.jsp";
    public static final String CATEGORIES_JSP = "jsp/logged/admin/categories.jsp";
    public static final String ADD_ACTIVITIES_FOR_USER_PAGE_JSP = "jsp/logged/admin/util/addActivitiesForUserPage.jsp";
    public static final String USER_DETAILED_JSP = "jsp/logged/admin/util/userDetailed.jsp";
    public static final String MANAGE_USERS_ACTIVITIES_PAGE_JSP = "jsp/logged/admin/util/manageUsersActivitiesPage.jsp";
    public static final String USER_ADD_PAGE = "jsp/logged/admin/util/userAddPage.jsp";
}
