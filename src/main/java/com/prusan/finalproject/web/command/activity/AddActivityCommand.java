package com.prusan.finalproject.web.command.activity;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.NameIsTakenException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.Validator;
import com.prusan.finalproject.web.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Add a new activity. Has validation for all fields.
 */
public class AddActivityCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final Validator validator = Validator.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");
        log.debug("retrieved an activity name '{}'", name);
        String desc = req.getParameter("description");
        log.debug("retrieved an activity description with length {}", desc.length());
        int catId = Integer.parseInt(req.getParameter("cId"));
        log.debug("retrieved a category id: {}", catId);

        HttpSession session = req.getSession();

        String referer = req.getHeader("referer");
        log.debug("retrieved a referer string: '{}'", referer);

        if (!doValidation(session, name, desc)) {
            Activity activity =  Activity.createWithoutIdAndUsersCount(name, desc, new Category(catId));
            session.setAttribute("invalidAddActivity", activity);
            log.debug("set a session attribute 'invalidAddActivity' ==> '{}'", activity);

            return Chain.createRedirect(referer);
        }

        log.debug("all fields are valid");
        Activity ac = Activity.createWithoutIdAndUsersCount(name, desc, new Category(catId));
        log.debug("created an activity instance {}", ac);

        ActivityService s = ServiceFactory.getInstance().getActivityService();
        try {
            s.add(ac);
            log.debug("successfully saved an activity {}", ac);
            session.removeAttribute("categories");
            session.removeAttribute("invalidActivity");

            return Chain.createRedirect(referer);
        } catch (NameIsTakenException ex) {
            log.debug("unable to add new activity {}, such activity already exists", ac);
            session.setAttribute("activityAddErrMsg", ex.getMessage());
            session.setAttribute("invalidAddActivity", ac);
            log.debug("set a session attribute 'invalidAddActivity' ==> '{}'", ac);

            return Chain.createRedirect(referer);
        } catch (ServiceException e) {
            log.error("error while trying to add new activity {}", ac, e);
            session.setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }
    }

    public static boolean doValidation(HttpSession session, String name, String desc) {
        boolean isValid = true;
        List<String> invalidFields = new ArrayList<>();

        if (!validator.validate(Validator.ACTIVITY_NAME, name)) {
            invalidFields.add("name");
            isValid = false;
        }

        if (!validator.validate(Validator.ACTIVITY_DESCRIPTION, desc)) {
            invalidFields.add("description");
            isValid = false;
        }

        if (!isValid) {
            String fields = String.join(", ", invalidFields.toArray(new String[] {}));
            session.setAttribute("invalidFields", fields);
            log.debug("set up a session attribute 'invalidFields': '{}'", fields);
        }


        return isValid;
    }
}