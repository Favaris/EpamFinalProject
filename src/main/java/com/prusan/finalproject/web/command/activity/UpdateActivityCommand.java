package com.prusan.finalproject.web.command.activity;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.PaginationAttributesHandler;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.command.CommandContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.prusan.finalproject.web.command.activity.AddActivityCommand.doValidation;

/**
 * Fully updates info about the given activity.
 */
public class UpdateActivityCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int activityId = Integer.parseInt(req.getParameter("id"));
        log.debug("got an activityId={}", activityId);
        String name = req.getParameter("name");
        log.debug("received an activity name '{}'", name);
        String desc = req.getParameter("description");
        log.debug("received an activity description with length {}", desc.length());
        int catId = Integer.parseInt(req.getParameter("cId"));
        log.debug("received a category id: {}", catId);

        if (!doValidation(req, name, desc)) {
            log.debug("input is invalid, sending back to activity editing page");
            return Chain.createRedirect(String.format("controller?command=%s&id=" + activityId, CommandContainer.CommandNames.SHOW_ACTIVITY_EDIT_PAGE));
        }

        ServiceFactory sf = ServiceFactory.getInstance();
        ActivityService as = sf.getActivityService();

        Activity activity = Activity.createWithoutUsersCount(activityId, name, desc, new Category(catId));
        log.debug("created an activity instance {}", activity);

        try {
            as.update(activity);
            log.debug("successfully updated an activity {}", activity);
            HttpSession s = req.getSession();
            s.removeAttribute("activityToEdit");
            s.removeAttribute("categories");
            log.debug("removed activity specific attributes from a session");

            String queryString = handler.getQueryString(s, true, true, true, false);
            log.debug("received a url params string: '{}'", queryString);

            return Chain.createRedirect(String.format("controller?command=%s&" + queryString, CommandContainer.CommandNames.SHOW_ACTIVITIES_PAGE));
        } catch (ServiceException e) {
            log.error("unable to update an activity {}", activity);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }
    }
}
