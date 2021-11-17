package com.prusan.finalproject.web.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Filter for cases when already logged user tries to sign in or sign up as another user.
 */
@WebFilter("/AuthFilter")
public class AuthFilter implements Filter {
    private static final Logger log = LogManager.getLogger(AuthFilter.class);
    private static final Set<String> commands;
    private static final String PATH = "jsp/guest/";

    static {
        commands = new HashSet<>();
        commands.add("signUp");
        commands.add("signIn");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("#init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        String command = httpReq.getParameter("command");
        HttpSession s = httpReq.getSession();
        if (s.getAttribute("user") != null && commands.contains(command)) {
            log.debug("logged user tries to {}, sending him to error page", command);
            s.setAttribute("err_msg", "true");
            httpResp.sendRedirect(PATH + "error.jsp");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
