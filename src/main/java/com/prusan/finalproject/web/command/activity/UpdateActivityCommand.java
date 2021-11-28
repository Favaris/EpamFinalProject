package com.prusan.finalproject.web.command.activity;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.command.util.DownloadAllActivitiesCommand;
import com.prusan.finalproject.web.command.util.DownloadAllCategoriesCommand;
import com.prusan.finalproject.web.command.util.PrepareForShowingAllActivitiesCommand;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.prusan.finalproject.web.command.activity.AddActivityCommand.createActivity;
import static com.prusan.finalproject.web.command.activity.AddActivityCommand.doValidation;

/**
 * Fully updates info about the given activity.
 */
public class UpdateActivityCommand implements Command {
    private static final Logger log = LogManager.getLogger(UpdateActivityCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int activityId = Integer.parseInt(req.getParameter("id"));
        log.debug("got an activityId={}", activityId);
        String name = req.getParameter("name");
        log.debug("got an activity name={}", name);
        String desc = req.getParameter("description");
        log.debug("got an activity description, desc length={}", desc.length());
        String[] catIds = req.getParameterValues("categoriesIds");
        log.debug("retrieved an array of categories' ids: {}", Arrays.toString(catIds));

        if (!doValidation(req, name, desc, catIds)) {
            log.debug("input is invalid, sending back to activity editing page");
            return new Chain("controller?command=showActivityEditPage&id=" + activityId, false);
        }

        ServiceFactory sf = ServiceFactory.getInstance();
        ActivityService as = sf.getActivityService();

        Activity activity = new Activity(activityId, name, desc);
        log.debug("created an activity instance {}", activity);
        List<Category> categories = new ArrayList<>();
        for (String id : catIds) {
            categories.add(new Category(Integer.parseInt(id)));
        }
        activity.setCategories(categories);
        log.debug("set a list of categories for activity {}, list size: {}", activity, categories.size());
        try {
            as.update(activity);
            log.debug("successfully updated an activity {}", activity);
            HttpSession s = req.getSession();
            s.removeAttribute("activityToEdit");
            s.removeAttribute("categories");
            log.debug("removed activity specific attributes from a session");
            return new Chain("controller?command=showActivitiesPage", false);
        } catch (ServiceException e) {
            log.error("unable to update an activity {}", activity);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        }
    }
}
