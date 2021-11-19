package com.prusan.finalproject.web.command;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Intermediate command for setting a list of all categories as the request attribute 'activities'.
 *
 * <pre> /////////THIS FEATURE IS TEMPORARY NOT IN USE
 *     WARN: for this command to work properly, you should firstly set to the request scope an attribute 'nextChain' with a Chain object that this command should call next
 * </pre>
 */
public class DownloadActivitiesCommand implements Command {
    private static final Logger log = LogManager.getLogger(DownloadActivitiesCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        ActivityService as = ServiceFactory.getInstance().getActivityService();

        try {
            List<Activity> activities = as.getAll();
            req.setAttribute("activities", activities);
            log.debug("set activities list as the request attribute");
        } catch (ServiceException e) {
            log.error("can not get all activities", e);
            req.setAttribute("err_msg", "can not get all activities");
            return new Chain(Pages.ERROR_JSP, true);
        }

        //return (Chain) req.getAttribute("nextChain");
        return new Chain(Pages.ACTIVITIES_JSP, true);
    }
}
