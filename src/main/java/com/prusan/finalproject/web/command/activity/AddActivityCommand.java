package com.prusan.finalproject.web.command.activity;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.NameIsTakenException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.PaginationAttributesHandler;
import com.prusan.finalproject.web.Validator;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.constant.ValidationErrorsFlags;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Add a new activity. Has validation for all fields.
 */
public class AddActivityCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final Validator validator = Validator.getInstance();
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");
        log.debug("retrieved an activity name '{}'", name);
        String desc = req.getParameter("description");
        log.debug("retrieved an activity description with length {}", desc.length());
        int catId = Integer.parseInt(req.getParameter("cId"));
        log.debug("retrieved a category id: {}", catId);

        HttpSession session = req.getSession();
        if (!doValidation(req, name, desc)) {
            Activity activity = createActivity(name, desc, catId);
            session.setAttribute("invalidActivity", activity);
            log.debug("set a session attribute 'invalidActivity' ==> '{}'", activity);
            return Chain.createRedirect("controller?command=showActivityAddPage");
        }

        log.debug("all fields are valid");
        Activity ac = createActivity(name, desc, catId);
        log.debug("created an activity instance {}", ac);

        ActivityService s = ServiceFactory.getInstance().getActivityService();
        try {
            s.save(ac);
            log.debug("successfully saved an activity {}", ac);
            session.removeAttribute("categories");
            session.removeAttribute("invalidActivity");

            String queryString = handler.getQueryStringWithSortingParameters(session);
            log.debug("received a query string: '{}'", queryString);

            return Chain.createRedirect("controller?command=showActivitiesPage&" + queryString);
        } catch (NameIsTakenException ex) {
            log.debug("unable to add new activity {}, such activity already exists", ac);
            session.setAttribute("invalidActivity", ac);
            session.setAttribute("err_msg", ex.getMessage());
            return Chain.createRedirect("controller?command=showActivityAddPage");
        } catch (ServiceException e) {
            log.error("error while trying to add new activity {}", ac, e);
            session.setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }
    }

    public static Activity createActivity(String name, String desc, int catId) {
        Activity ac = new Activity(name, desc);
        ac.setCategory(new Category(catId));
        return ac;
    }

    public static boolean doValidation(HttpServletRequest req, String name, String desc) {
        boolean isValid = true;
        HttpSession session = req.getSession();

        if (!validator.validate(Validator.ACTIVITY_NAME, name)) {
            session.setAttribute(ValidationErrorsFlags.ACTIVITY_NAME_ERROR_MESSAGE, "");
            log.debug("activity name is invalid, set a flag for corresponding error message as a session attribute '{}'", ValidationErrorsFlags.ACTIVITY_NAME_ERROR_MESSAGE);
            isValid = false;
        }

        if (!validator.validate(Validator.ACTIVITY_DESCRIPTION, desc)) {
            session.setAttribute(ValidationErrorsFlags.ACTIVITY_DESC_ERROR_MESSAGE, "");
            log.debug("activity description is invalid, set a flag for corresponding error message as a session attribute '{}'", ValidationErrorsFlags.ACTIVITY_DESC_ERROR_MESSAGE);
            isValid = false;
        }

        return isValid;
    }
}