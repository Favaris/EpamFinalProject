package com.prusan.finalproject.web.command;

import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command for accepting users' request, i.e. accepting for adding an activity or abandoning it. If the option was to abandon the activity, corresponding row in table is deleted.
 */
public class AcceptRequestCommand implements Command {
    private static final Logger log = LogManager.getLogger(AcceptRequestCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("retrieved a userId={}", userId);
        int activityId = Integer.parseInt(req.getParameter("aId"));
        log.debug("retrieved an activityId={}", activityId);

        ServiceFactory sf = ServiceFactory.getInstance();
        ActivityService as = sf.getActivityService();
        try {
            UserActivity ua = as.getUserActivity(userId, activityId);

            if (!ua.isAccepted()) {
                ua.setAccepted(true);
                as.updateUserActivity(ua);
                log.debug("accepted request for adding user activity {}", ua);
            } else {
                as.deleteUserActivity(ua.getUserId(), ua.getActivityId());
                log.debug("accepted request for abandoning user activity {}, activity deleted", ua);
            }
        } catch (ServiceException e) {
            log.error("unable to accept request", e);
            req.setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, true);
        }

        return new DownloadUsersRequestsCommand().execute(req, resp);
    }
}
