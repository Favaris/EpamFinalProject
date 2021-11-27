package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command for preparation for addition of new activities for the user by admin.
 */
public class PrepareForAdditionActivitiesForUserCommand implements Command {
    private static final Logger log = LogManager.getLogger(PrepareForAdditionActivitiesForUserCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        Chain nextChain = new Chain(Pages.ADD_ACTIVITIES_FOR_USER_PAGE_JSP, true);
        req.setAttribute("nextChain", nextChain);
        log.debug("set a request attribute 'nextChain' as {}", nextChain);
        return new DownloadAllActivitiesCommand().execute(req, resp);
    }
}
