package com.prusan.finalproject.web.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter for convenient pagination. Is used to remember the page number and other attributes relative to pagination and sorting/filtering from the previous request. Places them as request attributes.<br>
 * This filter helps some util commands such as 'DeleteActivityCommand' to return to the same page and with the same configurations as there were before calling this command.
 */
@WebFilter({"/logged", "/controller"})
public class PaginationFilter implements Filter {
    private static final Logger log = LogManager.getLogger(PaginationFilter.class);
    private static final Pattern disassembler = Pattern.compile(
            "^.*controller\\?command=(showActivitiesPage|showCategoriesPage)&page=([0-9]+)&pageSize=([0-9]+)(&orderBy=([^&]+))?(&filterBy=([^&]+))*$");
    private static final Pattern filtersPattern = Pattern.compile("&filterBy=([^&]+)*");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpSession session = httpReq.getSession();
        String prevPage = httpReq.getHeader("referer");
        log.debug("prevPage: '{}'", prevPage);

        Matcher m = disassembler.matcher(prevPage);

        if (!m.matches()) {
            log.debug("url does not match, moving forward");
            chain.doFilter(request, response);
            return;
        }

        log.debug("page matches");
        String page = m.group(2);
        log.debug("page ==> '{}', set as a session attribute 'page'", page);
        session.setAttribute("page", page);
        String pageSize = m.group(3);
        log.debug("pageSize ==> '{}', set as a session attribute 'pageSize'", pageSize);
        session.setAttribute("pageSize", pageSize);
        String orderBy = m.group(5);
        if (orderBy != null) {
            log.debug("orderBy ==> '{}', set as a session attribute 'orderBy'", orderBy);
            session.setAttribute("orderBy", orderBy);
        }
        if (m.group(6) != null) {
            log.debug("spotted some filters");
            Matcher filterMatcher = filtersPattern.matcher(prevPage);
            List<String> filters = new ArrayList<>();
            while (filterMatcher.find()) {
                String filter = filterMatcher.group(1);
                log.debug("filter ==> '{}'", filter);
                filters.add(filter);
            }
            log.debug("retrieved {} filters, set as a session attribute 'filterBy'", filters.size());
            session.setAttribute("filterBy", filters.toArray(new String[0]));
        }

        chain.doFilter(request, response);
    }
}
