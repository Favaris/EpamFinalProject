package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.service.UserActivityService;
import com.prusan.finalproject.db.service.UserService;
import com.prusan.finalproject.db.service.exception.NoSuchUserException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowDetailedUserInfoCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("retrieved a user id={}", userId);

        ServiceFactory sf = ServiceFactory.getInstance();
        UserService us = sf.getUserService();
        UserActivityService uas = sf.getUserActivityService();

        try {
            User user = us.getById(userId);
            log.debug("received a user {}", user);

            int activitiesCount = uas.getActivitiesCountForUser(userId, "all");
            log.debug("received amount of activities for user: {}", activitiesCount);

            int totalTime = uas.getSummarizedSpentTimeForUser(userId);
            log.debug("received total time spent by user on activities: {}", totalTime);

            req.setAttribute("userToShow", user);
            req.setAttribute("activitiesCount", activitiesCount);
            req.setAttribute("totalTime", totalTime);
            log.debug("set up all needed request attributes");

            return Chain.createForward(Pages.USER_DETAILED_JSP);
        } catch (NoSuchUserException ex) {
            log.error("unable to find user by id={}", userId, ex);
            req.getSession().setAttribute("err_msg", "Failed to find given user");
            return Chain.getErrorPageChain();
        } catch (ServiceException e) {
            log.error("failed to download all needed info about user with id={}", userId, e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }
    }
}
