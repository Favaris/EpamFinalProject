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
    private boolean includeSortingParameters;

    public void setIncludeSortingParameters(boolean includeSorting) {
        this.includeSortingParameters = includeSorting;
        log.debug("got set 'includeSortingParameters'={}", includeSorting);
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
        log.debug("retrieved a 'page' attribute: '{}}'", page);
        if (page == null) {
            page = 1;
            log.debug("page was null, setting the default value: page={}", page);
        }

        Integer pageSize = (Integer) req.getAttribute("pageSize");
        log.debug("retrieved a 'pageSize' attribute: '{}}'", pageSize);
        if (pageSize == null) {
            pageSize = 5;
            log.debug("pageSize was null, setting the default value: pageSize={}", pageSize);
        }

        if (!includeSortingParameters) {
            String queryString = String.format("page=%d&pageSize=%d", page, pageSize);
            log.debug("generated a query string: '{}'", queryString);

            session.setAttribute("paginationQueryString", queryString);
            log.debug("set query string as a session attribute");
            return;
        }

        String orderBy = (String) req.getAttribute("orderBy");
        log.debug("retrieved a 'orderBy' attribute: '{}'", orderBy);
        if (orderBy == null) {
            orderBy = "activityName";
            log.debug("orderBy was null, setting the default value: orderBy='{}'", orderBy);
        }

        String[] paramsfilterBy = (String[]) req.getAttribute("filterBy");
        log.debug("retrieved a 'filterBy' attribute: '{}'", Arrays.toString(paramsfilterBy));
        if (paramsfilterBy == null) {
            paramsfilterBy = new String[] { "all" };
            log.debug("filterBy was null, setting the default value: filterBy='{}'", Arrays.toString(paramsfilterBy));
        }
        String filterBy = String.join("&filterBy=", paramsfilterBy);

        String queryString = String.format("page=%d&pageSize=%d&orderBy=%s&filterBy=%s", page, pageSize, orderBy, filterBy);
        log.debug("generated a query string: '{}'", queryString);

        session.setAttribute("paginationQueryString", queryString);
        log.debug("set query string as a session attribute");
    }
}
