package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.NoSuchActivityException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Downloads an activity by its id, and all categories. Places them as a session attributes 'activityToEdit' and 'categories'.
 */
public class PrepareForActivityEditingCommand implements Command {
    private static final Logger log = LogManager.getLogger(PrepareForActivityEditingCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int activityId = Integer.parseInt(req.getParameter("id"));
        log.debug("retrieved an activityId={}", activityId);

        ServiceFactory sf = ServiceFactory.getInstance();
        ActivityService as = sf.getActivityService();

        try {
            Activity activity = as.getById(activityId);
            log.debug("retrieved an activity {}", activity);
            HttpSession s = req.getSession();
            s.setAttribute("activityToEdit", activity);
            req.setAttribute("nextChain", new Chain(Pages.ACTIVITY_EDIT_PAGE_JSP, true));
            return new DownloadAllCategoriesCommand().execute(req, resp);
        } catch (NoSuchActivityException e) {
            log.debug("no such activity with id={}", activityId, e);
            req.setAttribute("err_msg", "Activity you are looking for does not exist");
            return new Chain(Pages.ERROR_JSP, true);
        } catch (ServiceException e) {
            log.error("can not get an activity by id={}", activityId, e);
            req.setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, true);
        }
    }
}
