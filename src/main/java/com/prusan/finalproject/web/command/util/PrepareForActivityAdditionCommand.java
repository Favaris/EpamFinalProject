package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command that prepares everything that needed for the addition of a new Activity.
 */
public class PrepareForActivityAdditionCommand implements Command {
    private static final Logger log = LogManager.getLogger(PrepareForActivityAdditionCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        Chain nextChain = new Chain(Pages.ACTIVITY_ADD_PAGE_JSP, true);
        req.setAttribute("nextChain", nextChain);
        log.debug("set an attribute 'nextChain' ==> '{}'", nextChain);
        return new DownloadAllCategoriesCommand().execute(req, resp);
    }
}
