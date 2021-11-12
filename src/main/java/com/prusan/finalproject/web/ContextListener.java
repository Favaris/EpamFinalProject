package com.prusan.finalproject.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.*;

/**
 * Simple web listener that initializes the path to the logging file relatively to application real path.
 */
@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        String path = ctx.getRealPath("/WEB-INF/log4j2.log");
        System.setProperty("logFile", path);

        final Logger log = LogManager.getLogger(ContextListener.class);
        log.debug("logger file real path = {}", path);
    }

}
