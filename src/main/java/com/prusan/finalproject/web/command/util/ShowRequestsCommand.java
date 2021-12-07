package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.UserActivityService;
import com.prusan.finalproject.db.service.UserService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.PaginationAttributesHandler;
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
public class ShowRequestsCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        User currentUser = (User) req.getSession().getAttribute("user");
        int page = handler.getPageFromParameters(req);
        int pageSize = handler.getPageSizeFromParameters(req);

        ServiceFactory sf = ServiceFactory.getInstance();
        UserActivityService uas = sf.getUserActivityService();
        UserService us = sf.getUserService();

        try {
            List<UserActivity> requestedActivities;
            Map<UserActivity, String> requests = new HashMap<>();
            int entitiesCount;

            if ("user".equals(currentUser.getRole())) {
                log.debug("current user {} is default user, downloading all his requests", currentUser);
                requestedActivities = uas.getRequestedActivitiesForUser(currentUser.getId(), pageSize * (page - 1), pageSize);

                if (requestedActivities.isEmpty() && page > 1) {
                    log.debug("requested activities list was empty while page={}, reducing the page and redownloading the list", page);
                    --page;
                    requestedActivities =  uas.getRequestedActivitiesForUser(currentUser.getId(), pageSize * (page - 1), pageSize);
                }

                String login = currentUser.getLogin();
                for (UserActivity ua : requestedActivities) {
                    requests.put(ua, null);
                    log.debug("retrieved a new request: ({}, {})", ua, login);
                }

                entitiesCount = uas.getRequestsCountForUser(currentUser.getId());
            } else {
                log.debug("current user {} is an admin, downloading all users' requests", currentUser);
                requestedActivities = uas.getRequestedActivitiesForAllUsers(pageSize * (page - 1), pageSize);

                if (requestedActivities.isEmpty() && page > 1) {
                    log.debug("requested activities list was empty while page={}, reducing the page and redownloading the list", page);
                    --page;
                    requestedActivities =  uas.getRequestedActivitiesForAllUsers(pageSize * (page - 1), pageSize);
                }

                for (UserActivity ua : requestedActivities) {
                    User u = us.getById(ua.getUserId());
                    requests.put(ua, u.getLogin());
                    log.debug("retrieved a new request: ({}, {})", ua, u);
                }

                entitiesCount = uas.getRequestsCountForAdmin();
            }
            log.debug("received requests count for user {}, count={}", currentUser, entitiesCount);
            log.debug("got a map of requests, map size: {}", requests.size());

            req.setAttribute("requests", requests);
            log.debug("set attribute 'requests', map size={}", requests.size());
            handler.setPaginationParametersAsRequestAttributes(req, entitiesCount, pageSize, page, null, null);
        } catch (ServiceException e) {
            log.error("can not download requests", e);
            req.setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }

        return Chain.createForward(Pages.REQUESTS_JSP);
    }

}
