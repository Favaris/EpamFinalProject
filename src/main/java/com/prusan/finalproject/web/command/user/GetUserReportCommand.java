package com.prusan.finalproject.web.command.user;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.UserActivityService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetUserReportCommand implements Command {
    private static final Logger log = LogManager.getLogger(GetUserReportCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        ServiceFactory sf = ServiceFactory.getInstance();
        UserService us = sf.getUserService();
        UserActivityService uas = sf.getUserActivityService();

        try {
            Map<User, List<UserActivity>> report = new HashMap<>();
            for (User user : us.getWithRoleUser(0, Integer.MAX_VALUE)) {
                log.debug("retrieved a user {}", user);
                List<UserActivity> userActivities = uas.getAllAcceptedForUser(user.getId());
                log.debug("retrieved a list of all activities for user {}", user);
                report.put(user, userActivities);
                log.debug("put a user {} and list of his activities with size={} into the map", user, userActivities.size());
            }
            log.debug("successfully downloaded a user report, map size={}", report.size());

            req.setAttribute("report", report);
            return new Chain(Pages.USERS_REPORT_PAGE_JSP, true);
        } catch (ServiceException e) {
            log.error("failed to download a users report", e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        }
    }
}
