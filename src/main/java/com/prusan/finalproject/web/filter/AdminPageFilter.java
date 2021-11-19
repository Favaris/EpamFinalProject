package com.prusan.finalproject.web.filter;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.constant.DBConstants;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/" + Pages.USER_PAGE_JSP)
public class AdminPageFilter implements Filter {
    private static final Logger log = LogManager.getLogger(AdminPageFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession();

        User u = (User) session.getAttribute("user");

        if (u.getRole().equals(DBConstants.ADMIN)) {
            String contextPath = req.getServletContext().getContextPath() + "/";
            //req.setAttribute("nextChain", new Chain(contextPath + Pages.ADMIN_PAGE_JSP, false));
            //log.debug("sending admin {} to adminPage", u);
           // new Chain(contextPath + "controller?command=downloadActivities", true).moveForth(req, resp);
            resp.sendRedirect(contextPath + Pages.ADMIN_PAGE_JSP);
            return;
        }

        log.debug("user is not admin, doing nothing");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
