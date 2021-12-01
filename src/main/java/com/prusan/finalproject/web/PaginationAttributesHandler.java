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
    private static final Logger log = LogManager.getLogger(PaginationAttributesHandler.class);
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

    /**
     * Retrieves attributes 'page', 'pageSize', 'orderBy', 'filterBy' from the session and generates a proper string with url parameters representing these values.<br>
     * Sets default values to the attributes if there are no such in session attributes. Removes from the session all attributes mentioned above.
     * <pre>
     *     Default values for parameters:
     *     'page' ==> '1'
     *     'pageSize' ==> '5'
     *     'orderBy' ==> 'activityName'
     *     'filterBy' ==> 'all'
     * </pre>
     * @return a string containing url parameters for proper pagination.
     */
    public String getURLParametersStringWithSortingParams(HttpServletRequest req) {
        HttpSession session = req.getSession();
        String page = getPage(session);
        String pageSize = getPageSize(session);
        String orderBy = getOrderBy(session);
        String filterBy = getFilterBy(session);

        String urlParams = String.format("page=%s&pageSize=%s&orderBy=%s&filterBy=%s", page, pageSize, orderBy, filterBy);
        log.debug("generated a url parameters string with sorting parameters: '{}'", urlParams);

        removeSessionAttributes(session);

        return urlParams;
    }

    /**
     * Basically the same as {@link #getURLParametersStringWithSortingParams(HttpServletRequest)}, but without parameters relative to sorting and filtering.
     */
    public String getURLParametersString(HttpServletRequest req) {
        HttpSession session = req.getSession();
        String page = getPage(session);
        String pageSize = getPageSize(session);

        String urlParams = String.format("page=%s&pageSize=%s", page, pageSize);
        log.debug("generated a url parameters string: '{}'", urlParams);

        removeSessionAttributes(session);

        return urlParams;
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

    private void removeSessionAttributes(HttpSession session) {
        session.removeAttribute("page");
        session.removeAttribute("pageSize");
        session.removeAttribute("orderBy");
        session.removeAttribute("filterBy");
        log.debug("removed all used session parameters");
    }

    private String getPage(HttpSession session) {
        String page = (String) session.getAttribute("page");
        if (page == null) {
            page = DEFAULT_PAGE_NUMBER.toString();
            log.debug("did not find a session attribute 'page', setting the default value '{}'", page);
        } else {
            log.debug("retrieved a session attribute 'page': '{}'", page);
        }
        return page;
    }

    private String getPageSize(HttpSession session) {
        String pageSize = (String) session.getAttribute("pageSize");
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE.toString();
            log.debug("did not find a session attribute 'pageSize', setting the default value '{}'", pageSize);
        } else {
            log.debug("retrieved a session attribute 'pageSize': '{}'", pageSize);
        }
        return pageSize;
    }

    private String getOrderBy(HttpSession session) {
        String orderBy = (String) session.getAttribute("orderBy");
        if (orderBy == null) {
            orderBy = DEFAULT_ORDERING_RULE;
            log.debug("did not find a session attribute 'orderBy', setting the default value '{}'", orderBy);
        } else {
            log.debug("retrieved a session attribute 'orderBy': {}", orderBy);
        }
        return orderBy;
    }

    private String getFilterBy(HttpSession session) {
        String[] attrFilterBy = (String[]) session.getAttribute("filterBy");
        if (attrFilterBy == null) {
            attrFilterBy = DEFAULT_FILTERING_PARAMETERS;
            log.debug("did not find a session attribute 'filterBy', setting the default value '{}'", Arrays.toString(attrFilterBy));
        }

        log.debug("retrieved a session attribute 'filterBy': {}", Arrays.toString(attrFilterBy));
        String filterBy = String.join("&filterBy=", attrFilterBy);
        log.debug("got a 'filterBy' after join method: '{}'", filterBy);

        return filterBy;
    }
}
