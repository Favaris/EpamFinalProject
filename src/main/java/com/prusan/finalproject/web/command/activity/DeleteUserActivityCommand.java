package com.prusan.finalproject.web.command.activity;

import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.command.util.PrepareForUserEditingCommand;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command that deletes a user activity by user id and activity id. <br>
 * <pre>
 *     Retrieves next request parameters:
 *     'uId' - user id
 *     'aId' - activity id
 * </pre>
 */
public class DeleteUserActivityCommand implements Command {
    private static final Logger log = LogManager.getLogger(DeleteUserActivityCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("retrieved a userId={}", userId);
        int activityId = Integer.parseInt(req.getParameter("aId"));
        log.debug("retrieved an activityId={}", activityId);

        ActivityService as = ServiceFactory.getInstance().getActivityService();

        try {
            as.deleteUserActivity(userId, activityId);
            log.debug("successfully removed a user activity by userId={}, activityId={}", userId, activityId);
            return new Chain("controller?command=showEditUserPage&uId=" + userId, false);
        } catch (ServiceException e) {
            log.error("failed to remove a user activity by userId={}, activityId={}", userId, activityId, e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        }
    }
}
