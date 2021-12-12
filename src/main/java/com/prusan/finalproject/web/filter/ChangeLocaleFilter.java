package com.prusan.finalproject.web.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter that changes the locale. Redirects to the 'referer' header.
 */
@WebFilter("/changeLocale")
public class ChangeLocaleFilter implements Filter {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        log.debug("retrieved a HttpServletRequest instance: {}", req);
        HttpServletResponse resp = (HttpServletResponse) response;
        log.debug("retrieved a HttpServletResponse instance: {}", resp);

        String locale = req.getParameter("locale");
        log.debug("retrieved a locale parameter: '{}'", locale);

        req.getSession().setAttribute("javax.servlet.jsp.jstl.fmt.locale.session", locale);
        log.debug("changed 'javax.servlet.jsp.jstl.fmt.locale.session' attribute to '{}'", locale);

        String referer = req.getHeader("referer");
        log.debug("send redirect to '{}'", referer);
        resp.sendRedirect(referer);
    }
}
