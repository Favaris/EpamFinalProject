package com.prusan.finalproject.web.command.activity;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.NameIsTakenException;
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

        String referer = req.getHeader("referer");
        log.debug("retrieved a referer string: '{}'", referer);

        Activity activity = Activity.createWithoutUsersCount(activityId, name, desc, new Category(catId));
        log.debug("created an activity instance {}", activity);

        HttpSession session = req.getSession();
        if (!doValidation(session, name, desc)) {
            log.debug("input is invalid, sending back to activity editing page");
            session.setAttribute("invalidEditActivity", activity);
            log.debug("set a session attribute 'invalidEditActivity'");

            return Chain.createRedirect(referer);
        }

        ServiceFactory sf = ServiceFactory.getInstance();
        ActivityService as = sf.getActivityService();


        try {
            as.update(activity);
            log.debug("successfully updated an activity {}", activity);
            log.debug("removed activity specific attributes from a session");

            return Chain.createRedirect(referer);
        } catch (NameIsTakenException e) {
            log.error("activity with name {} is already taken", activity.getName(), e);
            session.setAttribute("invalidEditActivity", activity);
            session.setAttribute("activityEditErrMsg", e.getMessage());
            log.debug("set a session attribute 'invalidEditActivity'");

            return Chain.createRedirect(referer);
        } catch (ServiceException e) {
            log.error("unable to update an activity {}", activity);
            session.setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }
    }
}
