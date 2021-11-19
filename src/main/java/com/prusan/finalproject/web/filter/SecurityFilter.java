package com.prusan.finalproject.web.filter;

import com.prusan.finalproject.web.constant.DBConstants;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;


// @WebFilter("/*")
public class SecurityFilter implements Filter {
    private static final Logger log = LogManager.getLogger(SecurityFilter.class);
    private static final Map<String, Set<String>> permissions;

    static {
        permissions = new HashMap<>();
        Set<String> all = new HashSet<>(Arrays.asList(null, DBConstants.USER, DBConstants.ADMIN));
        Set<String> logged = new HashSet<>(Arrays.asList(DBConstants.USER, DBConstants.ADMIN));
        Set<String> admin = new HashSet<>();
        admin.add(DBConstants.ADMIN);

        permissions.put(Pages.INDEX_JSP, logged);
        //........
        //TODO:IMPLEMENT THIS PROPERLY!!!!!
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
