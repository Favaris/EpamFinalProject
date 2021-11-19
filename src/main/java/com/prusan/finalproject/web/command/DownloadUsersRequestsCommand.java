package com.prusan.finalproject.web.command;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.UserService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A command for downloading a map of users' requests. Places a Map of (User, UserActivity) as request parameter 'requests'.
 */
public class DownloadUsersRequestsCommand implements Command {
    private static final Logger log = LogManager.getLogger(DownloadActivitiesCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        ServiceFactory sf = ServiceFactory.getInstance();
        ActivityService as = sf.getActivityService();
        UserService us = sf.getUserService();

        try {
            List<UserActivity> list = as.getUsersRequests();

            Map<User, UserActivity> map = new HashMap<>();
            for (UserActivity ua : list) {
                User u = us.getById(ua.getUserId());
                map.put(u, ua);
                log.debug("retrieved a new request: ({}, {})", u, ua);
            }

            req.setAttribute("requests", map);
            log.debug("set attribute 'requests', map size={}", map.size());
        } catch (ServiceException e) {
            log.error("can not download requests", e);
            req.setAttribute("err_msg", "");
            return new Chain(Pages.ERROR_JSP, true);
        }

        return new Chain(Pages.REQUESTS_JSP, true);
    }
}