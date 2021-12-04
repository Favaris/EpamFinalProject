package com.prusan.finalproject.web.filter;

import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;


//@WebFilter("/*")
public class SecurityFilter implements Filter {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final Map<String, List<String>> permissions;

    static {
        permissions = new HashMap<>();

        List<String> adminPages = Arrays.asList(
                Pages.USERS_JSP,
                Pages.USER_EDIT_PAGE_JSP,
                Pages.ACTIVITY_EDIT_PAGE_JSP,
                Pages.ACTIVITY_ADD_PAGE_JSP,
                Pages.CATEGORIES_JSP);
        permissions.put("admin", adminPages);

        List<String> userPages = Arrays.asList(
                Pages.RUNNING_ACTIVITIES_JSP
        );
        permissions.put("user", adminPages);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        HttpSession s = httpReq.getSession();
        if (s.getAttribute("user") == null) {
            log.debug("sent unauthorized user to auth_persistence page");
            httpResp.sendRedirect("auth_persistence.jsp");
        }
        log.debug("user is authorized, access granted");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
