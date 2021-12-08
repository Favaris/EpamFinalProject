package com.prusan.finalproject.web.command.util;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowUsersReportCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        ServiceFactory sf = ServiceFactory.getInstance();
        UserService us = sf.getUserService();
        UserActivityService uas = sf.getUserActivityService();

        try {
            List<User> users = us.getAllWithRoleUser();
            log.debug("received a list of all default users, list size: {}", users.size());

            List<Integer> activitiesCounts = new ArrayList<>(users.size());
            List<Integer> totalTimeList = new ArrayList<>(users.size());
            String[] filterByStub = {"all"};
            for (User u : users) {
                int activitiesCount = uas.getActivitiesCountForUser(u.getId(), filterByStub);
                activitiesCounts.add(activitiesCount);
                int totalTime = uas.getSummarizedSpentTimeForUser(u.getId());
                totalTimeList.add(totalTime);
                log.debug("received a total time spent={} and activities count={} for user {}", totalTime, activitiesCount, u);
            }

            req.setAttribute("users", users);
            req.setAttribute("activitiesCounts", activitiesCounts);
            req.setAttribute("totalTimeList", totalTimeList);
            log.debug("set up all needed request attributes");
            return Chain.createForward(Pages.USERS_REPORT_PAGE_JSP);
        } catch (ServiceException e) {
            log.error("failed to download a users report", e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }
    }
}