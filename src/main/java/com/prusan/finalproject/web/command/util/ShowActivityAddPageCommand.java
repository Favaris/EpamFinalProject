package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.CommandUtils;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command that prepares everything that needed for the addition of a new Activity.
 */
public class ShowActivityAddPageCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final CommandUtils commandUtils = CommandUtils.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            commandUtils.setAllCategoriesInRequestAttribute(req);
            return Chain.createForward(Pages.ACTIVITY_ADD_PAGE_JSP);
        } catch (ServiceException e) {
            log.error("failed to download all categories", e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }
    }
}
