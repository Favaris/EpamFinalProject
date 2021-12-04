package com.prusan.finalproject.web.filter;

import com.prusan.finalproject.web.constant.Pages;
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
public class AuthFilter implements Filter {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final Set<String> commands;

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
            httpResp.sendRedirect(Pages.ERROR_JSP);
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
