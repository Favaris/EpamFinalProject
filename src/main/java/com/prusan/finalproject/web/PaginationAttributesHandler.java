package com.prusan.finalproject.web;

import com.prusan.finalproject.db.entity.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * Class that retrieves pagination relative attributes from the HttpServletRequest and returns corresponding values.
 */
public class PaginationAttributesHandler {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static PaginationAttributesHandler instance;

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
        String orderBy = req.getParameter("orderBy");
        log.debug("retrieved an 'orderBy' parameter: '{}'", orderBy);
        if (orderBy == null) {
            orderBy = DEFAULT_ORDERING_RULE;
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

    /**
     * Utility method for getting a pagination sublist from a list of all entities.
     * @param entities list of all entities
     * @param page page number
     * @param pageSize page size (number of elements displayed on the page)
     */
    public <E> List<E> getPaginationSublist(List<E> entities, int page, int pageSize) {
        List<E> paginatedList = entities.subList(pageSize * (page - 1), Math.min(pageSize * page, entities.size()));
        log.debug("got a sublist for pagination, list size={}", paginatedList.size());
        if (paginatedList.size() == 0 && page > 1) {
            log.debug("sublist is empty while page={}, reducing the page count and making a new sublist", page);
            --page;
            paginatedList = getPaginationSublist(entities, page, pageSize);
        }
        return paginatedList;
    }

    /**
     * Retrieves a session attribute 'paginationQueryString' and returns it. If the attribute is missing, returns query string with the default parameters' values.
     * @return session.getAttribute("paginationQueryString") or the default query string if this parameter is missing.
     */
    public String getQueryStringWithSortingParameters(HttpSession session) {
        String queryString = (String) session.getAttribute("paginationQueryString");
        log.debug("retrieved a query string: '{}'", queryString);
        if (queryString == null) {
            queryString = String.format("page=%d&pageSize=%d&orderBy=%s&filterBy=%s", DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, DEFAULT_ORDERING_RULE, String.join("&filterBy=", DEFAULT_FILTERING_PARAMETERS));
            log.debug("query string attribute was null, set default value: '{}'", queryString);
        }
        return queryString;
    }

    public String getQueryString(HttpSession session) {
        String queryString = (String) session.getAttribute("paginationQueryString");
        log.debug("retrieved a query string: '{}'", queryString);
        if (queryString == null) {
            queryString = String.format("page=%d&pageSize=%d", DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
            log.debug("query string attribute was null, set default value: '{}'", queryString);
        }
        return queryString;
    }

}
