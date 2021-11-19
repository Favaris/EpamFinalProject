package com.prusan.finalproject.web.command;

import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Command for signing out.
 */
public class SignOutCommand implements Command {
    private static final Logger log = LogManager.getLogger(SignOutCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();

        if (session.getAttribute("user") != null) {
            session.invalidate();
            log.debug("session invalidated: {}", session);
        }

        return new Chain(Pages.INDEX_JSP, false);
    }
}
