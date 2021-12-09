package com.prusan.finalproject.web.tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Arrays;

/**
 * This tag retrieves request attributes such as 'page', 'pageSize', 'orderBy', 'filterBy' and formats a query string using these values as parameters, places it as a session attribute 'paginationQueryString'.<br>
 * Tag is useful for some util commands such as 'delete activity command' in a way that it helps them to return to the same page with the same sorting/filtering config as it was before calling them.
 */
public class PaginationHelperTag extends SimpleTagSupport {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private boolean includeSorting;
    private boolean includeFiltering;
    private boolean includeSearching;

    public void setIncludeSorting(boolean includeSorting) {
        this.includeSorting = includeSorting;
        log.debug("got set 'includeSorting'={}", includeSorting);
    }

    public void setIncludeFiltering(boolean includeFiltering) {
        this.includeFiltering = includeFiltering;
        log.debug("got set 'includeFiltering'={}", includeSorting);
    }

    public void setIncludeSearching(boolean includeSearching) {
        this.includeSearching = includeSearching;
        log.debug("got set 'includeSearching'={}", includeSorting);
    }

    @Override
    public void doTag() throws JspException, IOException {
        PageContext pageContext = (PageContext) getJspContext();
        log.debug("retrieved a page context instance {}", pageContext);
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        log.debug("retrieved a servlet request instance {}", req);
        HttpSession session = req.getSession();
        log.debug("retrieved a session instance {}", session);

        Integer page = (Integer) req.getAttribute("page");
        log.debug("retrieved a 'page' attribute: '{}'", page);

        Integer pageSize = (Integer) req.getAttribute("pageSize");
        log.debug("retrieved a 'pageSize' attribute: '{}'", pageSize);

        String queryString = String.format("page=%d&pageSize=%d", page, pageSize);
        log.debug("generated a query string: '{}'", queryString);

        session.setAttribute("paginationQueryString", queryString);
        log.debug("set pagination query string as a session attribute");

        if (includeSorting) {
                    setUpSortingString(req);
        }

        if (includeFiltering) {
            setUpFilteringString(req);
        }

        if (includeSearching) {
            setUpSearchingString(req);
        }
    }

    private void setUpSearchingString(HttpServletRequest req) {
        String countLessThen = (String) req.getAttribute("countLessThen");
        log.debug("retrieved a 'countLessThen' attribute: {}", countLessThen);


        String countBiggerThen = (String) req.getAttribute("countBiggerThen");
        log.debug("retrieved a 'countBiggerThen' attribute: {}", countBiggerThen);


        String searchBy = (String) req.getAttribute("searchBy");
        log.debug("retrieved a 'searchBy' attribute: {}", searchBy);


        String queryString = String.format("countLessThen=%s&countBiggerThen=%s&searchBy=%s", countLessThen, countBiggerThen, searchBy);
        req.getSession().setAttribute("searchingQueryString", queryString);
        log.debug("set a 'searchingQueryString' session attribute: '{}'", queryString);
    }

    private void setUpFilteringString(HttpServletRequest req) {
        String[] paramsfilterBy = (String[]) req.getAttribute("filterBy");
        log.debug("retrieved a 'filterBy' attribute: '{}'", Arrays.toString(paramsfilterBy));

        String filterBy = String.join("&filterBy=", paramsfilterBy);

        String queryString = String.format("filterBy=%s", filterBy);

        req.getSession().setAttribute("filteringQueryString", queryString);
        log.debug("set a 'filteringQueryString' session attribute: '{}'", queryString);
    }

    private void setUpSortingString(HttpServletRequest req) {
        String orderBy = (String) req.getAttribute("orderBy");
        log.debug("retrieved a 'orderBy' attribute: '{}'", orderBy);

        String queryString = String.format("orderBy=%s", orderBy);
        req.getSession().setAttribute("sortingQueryString", queryString);
        log.debug("set a 'sortingQueryString' session attribute: '{}'", queryString);
    }
}
