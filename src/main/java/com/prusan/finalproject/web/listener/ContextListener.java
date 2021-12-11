package com.prusan.finalproject.web.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Simple listener that initializes the path to the logging file relatively to application real path and loads localization property file as the context attribute.
 */
@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        // logger set up
        String path = ctx.getRealPath("/WEB-INF/log4j2.log");
        System.setProperty("logFile", path);

        final Logger log = LogManager.getLogger(ContextListener.class);
        log.debug("logger file real path: '{}'", path);
        String root = ctx.getContextPath();
        ctx.setAttribute("root", root);

        //localisation set up
        String localesFileName = ctx.getInitParameter("locales");
        String localesFileRealPath = ctx.getRealPath(localesFileName);
        log.debug("locales file real path: '{}'", localesFileRealPath);

        Properties locales = new Properties();
        try {
            locales.load(new FileInputStream(localesFileRealPath));
        } catch (IOException e) {
            log.fatal("failed to load locales file", e);
        }

        ctx.setAttribute("locales", locales);
        log.debug("set context attribute 'locales': {}", locales);
    }
}