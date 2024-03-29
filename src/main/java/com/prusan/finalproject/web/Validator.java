package com.prusan.finalproject.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validator is a singleton class, which validates specific data. It contains a map with the name of field to validate and a regexp with which it will be validated.
 * To validate data, use method 'validate', passing to it one of constants representing field names, and a value to check.
 */
public class Validator {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static Validator instance;
    private final Map<Integer, Pattern> fields;

    public static final Integer USER_LOGIN = 1;
    public static final Integer USER_PASSWORD = 2;
    public static final Integer USER_NAME = 3;
    public static final Integer USER_SURNAME = 3; // for convenience, same as USER_NAME
    public static final Integer ACTIVITY_NAME = 4;
    public static final Integer ACTIVITY_DESCRIPTION = 5;
    public static final Integer CATEGORY_NAME = 4; // for convenience, same as ACTIVITY_NAME

    public static synchronized Validator getInstance() {
        if (instance == null) {
            instance = new Validator();
        }
        return instance;
    }

    private Validator() {
        fields = new HashMap<>();
        // validating pattern for user login
        fields.put(USER_LOGIN, Pattern.compile("[A-Za-z0-9]{4,16}?"));
        // validating pattern for user password (could be replaced later)
        fields.put(USER_PASSWORD, Pattern.compile(".{4,32}?"));
        // validating pattern for user's name AND surname
        fields.put(USER_NAME, Pattern.compile("([A-Z][a-z]{1,30}|[А-ЯІЇЄЁ][а-яіїєґё]{1,30})"));
        // validating pattern for activity's name
        fields.put(ACTIVITY_NAME, Pattern.compile("[A-ZА-ЯІЇЄЁa-zа-яіїєґё 0-9]{1,30}"));
        // validating pattern for activity's description
        fields.put(ACTIVITY_DESCRIPTION, Pattern.compile(".{1,1000}"));
    }

    public boolean validate(Integer field, String value) {
        if (value == null) {
            return false;
        }

        Pattern p = fields.get(field);
        if (p == null) {
            log.warn("no pattern found for a field: {}", field);
            throw new IllegalArgumentException("No pattern for a field " + field);
        }
        log.debug("retrieved a pattern for a field {}", field);
        Matcher m = p.matcher(value);
        boolean matches = m.matches();
        log.debug("result of validating field {} with value {} is {}", field, value, matches);
        return matches;
    }
}
