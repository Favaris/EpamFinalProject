package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.ActivityService;
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
import java.util.stream.Collectors;

/**
 * A command for downloading a map of users' requests.
 * <br>
 * <pre>If current user is admin, downloads all requests from all users; if it is a default user, then downloads only his requests.</pre><br>
 * Places a Map of (User, UserActivity) as request parameter 'requests'.
 */
public class DownloadUsersRequestsCommand implements Command {
    private static final Logger log = LogManager.getLogger(DownloadAllActivitiesCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        User currentUser = (User) req.getSession().getAttribute("user");

        ServiceFactory sf = ServiceFactory.getInstance();
        UserActivityService uas = sf.getUserActivityService();
        UserService us = sf.getUserService();

        try {
            List<UserActivity> list = uas.getUsersRequests();
            log.debug("retrieved a list of all users' requested activities");

            Map<UserActivity, User> map = new HashMap<>();
            if ("user".equals(currentUser.getRole())) {
                log.debug("current user {} is default user, filtering out all activities that are not his", currentUser);
                list = list.stream()
                        .filter(userActivity -> userActivity.getUserId().equals(currentUser.getId()))
                        .collect(Collectors.toList());
                log.debug("filtered out all activities that do not belong to user {}", currentUser);

                for (UserActivity ua : list) {
                    map.put(ua, currentUser);
                }
            } else {
                for (UserActivity ua : list) {
                    User u = us.getById(ua.getUserId());
                    map.put(ua, u);
                    log.debug("retrieved a new request: ({}, {})", ua, u);
                }
            }
            log.debug("got a map of requests ");

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
