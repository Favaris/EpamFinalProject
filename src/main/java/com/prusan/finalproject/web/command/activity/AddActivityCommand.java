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
import com.prusan.finalproject.web.command.util.DownloadAllActivitiesCommand;
import com.prusan.finalproject.web.command.util.DownloadAllCategoriesCommand;
import com.prusan.finalproject.web.constant.ValidationErrorsFlags;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Add a new activity. Has validation for all fields.
 */
public class AddActivityCommand implements Command {
    private static final Logger log = LogManager.getLogger(AddActivityCommand.class);
    private static final Validator validator = Validator.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");
        log.debug("retrieved an activity name '{}'", name);
        String desc = req.getParameter("description");
        log.debug("retrieved an activity description with length {}", desc.length());
        String[] catIds = req.getParameterValues("categoriesIds");
        log.debug("retrieved an array of categories' ids: {}", Arrays.toString(catIds));

        if (!doValidation(req, name, desc, catIds)) {
            HttpSession session = req.getSession();
            session.setAttribute("invalidActivity", createActivity(name, desc, catIds));
            return new DownloadAllCategoriesCommand().execute(req, resp);
        }

        Activity ac = createActivity(name, desc, catIds);
        log.debug("created an activity instance {}", ac);

        ActivityService s = ServiceFactory.getInstance().getActivityService();
        try {
            s.save(ac);
            log.debug("successfully saved an activity {}", ac);
            req.getSession().removeAttribute("categories");
            return new DownloadAllActivitiesCommand().execute(req, resp);
        } catch (NameIsTakenException ex) {
            log.debug("unable to add new activity {}, such activity already exists", ac);
            HttpSession session = req.getSession();
            session.setAttribute("invalidActivity", ac);
            session.setAttribute("err_msg", ex.getMessage());
            return new DownloadAllCategoriesCommand().execute(req, resp);
        } catch (ServiceException e) {
            log.error("error while trying to add new activity {}", ac, e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        }
    }

    private Activity createActivity(String name, String desc, String[] catIds) {
        Activity ac = new Activity(name, desc);
        List<Category> cats = new ArrayList<>();
        if (catIds != null) {
            for (String id : catIds) {
                cats.add(new Category(Integer.parseInt(id)));
            }
        }
        ac.setCategories(cats);
        return ac;
    }

    private boolean doValidation(HttpServletRequest req, String name, String desc, String[] catIds) {
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

        if (catIds == null) {
            session.setAttribute(ValidationErrorsFlags.ACTIVITY_CATEGORIES_ERROR_MESSAGE, "");
            log.debug("categories are empty, set a flag for corresponding error message as a session attribute '{}'", ValidationErrorsFlags.ACTIVITY_CATEGORIES_ERROR_MESSAGE);
            isValid = false;
        }

        return isValid;
    }
}