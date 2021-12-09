package com.prusan.finalproject.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

/**
 * Class that retrieves pagination relative attributes from the HttpServletRequest and returns corresponding values.
 */
public class PaginationAttributesHandler {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static PaginationAttributesHandler instance;

    public static final String DEFAULT_SEARCH_BY = "";
    public static final String DEFAULT_BIGGER_THEN = "";
    public static final String DEFAULT_LESS_THEN = "";
    public static final Integer DEFAULT_PAGE_NUMBER = 1;
    public static final Integer DEFAULT_PAGE_SIZE = 5;
    public static final String DEFAULT_ORDERING_RULE = "activityName";
    public static final String[] DEFAULT_FILTERING_PARAMETERS = new String[] {"all"};

    private PaginationAttributesHandler() {}

    public static synchronized PaginationAttributesHandler getInstance() {
        if (instance == null) {
            instance = new PaginationAttributesHandler();
        }
        return instance;
    }


    public int getPageFromParameters(HttpServletRequest req) {
        String paramPage = req.getParameter("page");
        log.debug("retrieved a 'page' parameter: '{}'", paramPage);
        int page = DEFAULT_PAGE_NUMBER;
        if (paramPage != null) {
            page = Integer.parseInt(paramPage);
        }
        log.debug("returned a page number: {}", page);
        return page;
    }

    public int getPageSizeFromParameters(HttpServletRequest req) {
        String paramPageSize = req.getParameter("pageSize");
        log.debug("retrieved a 'pageSize' parameter: '{}'", paramPageSize);
        int pageSize = DEFAULT_PAGE_SIZE;
        if (paramPageSize != null) {
            pageSize = Integer.parseInt(paramPageSize);
        }
        log.debug("returned a page number: {}", pageSize);
        return pageSize;
    }

    public String getOrderByFromParameters(HttpServletRequest req) {
        return getOrderByFromParameters(req, DEFAULT_ORDERING_RULE);
    }

    public String getOrderByFromParameters(HttpServletRequest req, String defaultRule) {
        String orderBy = req.getParameter("orderBy");
        log.debug("retrieved an 'orderBy' parameter: '{}'", orderBy);
        if (orderBy == null) {
            orderBy = defaultRule;
            log.debug("set default value to orderBy: '{}'", orderBy);
        }
        return orderBy;
    }

    public String[] getFilterByFromParameters(HttpServletRequest req) {
        String[] filterBy = req.getParameterValues("filterBy");
        log.debug("retrieved an 'filterBy' parameter: '{}'", Arrays.toString(filterBy));
        if (filterBy == null) {
            filterBy = DEFAULT_FILTERING_PARAMETERS;
            log.debug("set default value to filterBy: '{}'", filterBy[0]);
        }
        return filterBy;
    }

    public String getCountLessThenFromParameters(HttpServletRequest req) {
        return getCountLessThenFromParameters(req, DEFAULT_LESS_THEN);
    }

    public String getCountLessThenFromParameters(HttpServletRequest req, String defaultValue) {
        String countLessThen = req.getParameter("countLessThen");
        log.debug("retrieved a 'countLessThen' parameter: {}", countLessThen);
        if (countLessThen == null) {
            countLessThen = defaultValue;
            log.debug("set default value to countLessThen: '{}'", countLessThen);
        }
        log.debug("returned a countLessThen: '{}'", countLessThen);
        return countLessThen;
    }

    public String getCountBiggerThenFromParameters(HttpServletRequest req) {
        return getCountBiggerThenFromParameters(req, DEFAULT_BIGGER_THEN);
    }

    public String getCountBiggerThenFromParameters(HttpServletRequest req, String defaultValue) {
        String countBiggerThen = req.getParameter("countBiggerThen");
        log.debug("retrieved a 'countBiggerThen' parameter: {}", countBiggerThen);
        if (countBiggerThen == null) {
            countBiggerThen = defaultValue;
            log.debug("set default value to countBiggerThen: '{}'", countBiggerThen);
        }
        log.debug("returned a countBiggerThen: '{}'", countBiggerThen);
        return countBiggerThen;
    }

    public String getSearchByFromParameters(HttpServletRequest req) {
        return getSearchByFromParameters(req, DEFAULT_SEARCH_BY);
    }

    public String getSearchByFromParameters(HttpServletRequest req, String defaultValue) {
        String searchBy = req.getParameter("searchBy");
        log.debug("retrieved a 'searchBy' parameter: {}", searchBy);
        if (searchBy == null) {
            searchBy = defaultValue;
            log.debug("set default value to searchBy: '{}'", searchBy);
        }
        return searchBy;
    }

    /**
     * Sets all pagination relative parameters as request attributes. Calculates 'pageCount' attribute's value by 'entitiesCount' and 'pageSize' parameters.<br>
     * Doesn't set 'orderBy' and 'filterBy' attributes if there were passed nulls.
     * <pre>
     * Sets next attributes in the given request:
     * 'pageCount' - calculated using 'entitiesCount' and 'pageSize' parameters
     * 'page' - number of current page
     * 'orderBy' - ordering rule
     * 'filterBy' - a set of filters
     * </pre>
     */
    public void setPaginationParametersAsRequestAttributes(HttpServletRequest req, int entitiesCount, int pageSize, int page, String orderBy, String[] filterBy) {
        int pageCount = entitiesCount / pageSize;
        pageCount += entitiesCount % pageSize == 0 ? 0 : 1;
        req.setAttribute("pageCount" , pageCount);
        log.debug("set a 'pageCount' attribute: '{}'", pageCount);
        req.setAttribute("page", page);
        log.debug("set a 'page' attribute: '{}'", page);
        if (orderBy != null) {
            req.setAttribute("orderBy", orderBy);
            log.debug("set an 'orderBy' attribute: '{}'", orderBy);
        }
        if (filterBy != null) {
            req.setAttribute("filterBy", filterBy);
            log.debug("set a 'filterBy' attribute: '{}'", orderBy);
        }
    }

    public String getSearchingQueryString(HttpSession session) {
        String queryString = (String) session.getAttribute("searchingQueryString");
        log.debug("retrieved a 'searchingQueryString' attribute: '{}'", queryString);
        if (queryString == null) {
            queryString = String.format("countLessThen=%s&countBiggerThen=%s&searchBy=%s", DEFAULT_LESS_THEN, DEFAULT_BIGGER_THEN, DEFAULT_SEARCH_BY);
            log.debug("query string attribute was null, set default value: '{}'", queryString);
        }
        return queryString;
    }

    public String getFilteringQueryString(HttpSession session) {
        String queryString = (String) session.getAttribute("filteringQueryString");
        log.debug("retrieved a 'filteringQueryString' attribute: '{}'", queryString);
        if (queryString == null) {
            queryString = String.format("filterBy=%s", String.join("&filterBy=", DEFAULT_FILTERING_PARAMETERS));
            log.debug("query string attribute was null, set default value: '{}'", queryString);
        }
        return queryString;
    }

    /**
     * Retrieves a session attribute 'paginationQueryString' and returns it. If the attribute is missing, returns query string with the default parameters' values.
     * @return session.getAttribute("paginationQueryString") or the default query string if this parameter is missing.
     */
    public String getSortingQueryString(HttpSession session) {
        String queryString = (String) session.getAttribute("sortingQueryString");
        log.debug("retrieved a 'sortingQueryString' attribute: '{}'", queryString);
        if (queryString == null) {
            queryString = String.format("orderBy=%s", DEFAULT_ORDERING_RULE);
            log.debug("query string attribute was null, set default value: '{}'", queryString);
        }
        return queryString;
    }

    public String getPaginationQueryString(HttpSession session) {
        String queryString = (String) session.getAttribute("paginationQueryString");
        log.debug("retrieved a 'paginationQueryString' attribute: '{}'", queryString);
        if (queryString == null) {
            queryString = String.format("page=%d&pageSize=%d", DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
            log.debug("query string attribute was null, set default value: '{}'", queryString);
        }
        return queryString;
    }

    public String getQueryString(HttpSession session, boolean includePagination, boolean includeSorting, boolean includeFiltering, boolean includeSearching) {
        StringBuilder builder = new StringBuilder();
        if (includePagination) {
            builder.append(getPaginationQueryString(session)).append("&");
        }
        if (includeSorting) {
            builder.append(getSortingQueryString(session)).append("&");
        }
        if (includeFiltering) {
            builder.append(getFilteringQueryString(session)).append("&");
        }
        if (includeSearching) {
            builder.append(getSearchingQueryString(session)).append("&");
        }
        String query = builder.toString();
        log.debug("generated a query string: '{}'", query);
        return query;
    }
}
