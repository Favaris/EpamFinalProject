package com.prusan.finalproject.web.constant;

/**
 * Abstract class with static final fields for relative path to the pages.
 */
public abstract class Pages {
    private Pages() {}


    public static final String ACTIVITIES_JSP = "jsp/activities.jsp";
    public static final String REQUESTS_JSP = "jsp/requests.jsp";
    public static final String SIGN_IN_JSP = "jsp/signIn.jsp";
    public static final String SIGN_UP_JSP = "jsp/signUp.jsp";
    public static final String ERROR_JSP = "jsp/error.jsp";

    public static final String USER_PAGE_JSP = "jsp/user/userPage.jsp";
    public static final String RUNNING_ACTIVITIES_JSP = "jsp/user/runningActivities.jsp";

    public static final String ADMIN_PAGE_JSP = "jsp/admin/adminPage.jsp";
    public static final String USERS_JSP = "jsp/admin/users.jsp";
    public static final String USER_EDIT_PAGE_JSP = "jsp/admin/util/userEditPage.jsp";
    public static final String ACTIVITY_EDIT_PAGE_JSP = "jsp/admin/util/activityEditPage.jsp";
    public static final String ACTIVITY_ADD_PAGE_JSP = "jsp/admin/util/activityAddPage.jsp";
    public static final String CATEGORIES_JSP = "jsp/admin/categories.jsp";
}
