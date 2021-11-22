package com.prusan.finalproject.web.command;

import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteActivityCommand implements Command {
    private static final Logger log = LogManager.getLogger(DeleteActivityCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int activityId = Integer.parseInt(req.getParameter("id"));
        log.debug("retrieved an activityId={}", activityId);

        ActivityService as = ServiceFactory.getInstance().getActivityService();
        try {
            as.delete(activityId);
            log.debug("deleted an activity with id={}", activityId);
            return new DownloadAllActivitiesCommand().execute(req, resp);
        } catch (ServiceException e) {
            log.error("unable to delete an activity by id={}", activityId, e);
            req.getSession().setAttribute("err_msg", "Can not delete this activity");
            return new Chain(Pages.ERROR_JSP, false);
        }
    }
}
