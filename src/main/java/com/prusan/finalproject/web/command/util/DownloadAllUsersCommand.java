package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.service.UserService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Downloads all users with role 'user' as List in request attribute 'usersList'.
 */
public class DownloadAllUsersCommand implements Command {
    private static final Logger log = LogManager.getLogger(DownloadAllUsersCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        UserService us = ServiceFactory.getInstance().getUserService();

        try {
            List<User> usersList = us.getAllWithRoleUser();
            log.debug("got a usersList, list size: {}", usersList.size());
            req.setAttribute("usersList", usersList);
            return new Chain(Pages.USERS_JSP, true);
        } catch (ServiceException e) {
            log.error("error while trying to download all users with role='user'", e);
            req.setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, true);
        }
    }
}
