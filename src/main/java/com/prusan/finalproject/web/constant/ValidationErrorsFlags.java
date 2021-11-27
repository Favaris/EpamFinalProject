package com.prusan.finalproject.web.constant;

/**
 * This class contains names of attributes that are used as "flags" as session attributes. If there is an attribute with name as one of fields of this class, then the corresponding error message should be presented on the view layer.
 * Only used for validation errors.
 */
public abstract class ValidationErrorsFlags {
    private ValidationErrorsFlags() {}

    public static final String LOGIN_ERROR_MESSAGE = "loginErrorMessage";
    public static final String PASSWORD_ERROR_MESSAGE = "passwordErrorMessage";
    public static final String USER_NAME_ERROR_MESSAGE = "nameErrorMessage";
    public static final String USER_SURNAME_ERROR_MESSAGE = "surnameErrorMessage";

    public static final String ACTIVITY_NAME_ERROR_MESSAGE = "activityNameErrorMessage";
    public static final String ACTIVITY_DESC_ERROR_MESSAGE = "activityDescErrorMessage";
    public static final String ACTIVITY_CATEGORIES_ERROR_MESSAGE = "activityCatsErrorMessage";
}
