package com.prusan.finalproject.web;

import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.CategoryService;
import com.prusan.finalproject.db.service.UserActivityService;
import com.prusan.finalproject.db.service.UserService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Utility class for Command implementors. Contains methods often used by Commands (for example, a method to get all categories, which is used and for filtering and for showing all categories).<br>
 * Singleton.
 */
public class CommandUtils {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static CommandUtils instance;

    private UserService userService;
    private ActivityService activityService;
    private UserActivityService userActivityService;
    private CategoryService categoryService;

    private CommandUtils() {
        ServiceFactory sf = ServiceFactory.getInstance();
        userService = sf.getUserService();
        activityService = sf.getActivityService();
        userActivityService = sf.getUserActivityService();
        categoryService = sf.getCategoryService();
    }

    public static synchronized CommandUtils getInstance() {
        if (instance == null) {
            instance = new CommandUtils();
        }
        return instance;
    }

    /**
     * Sets a list of all categories as a request attribute 'categories'.
     * @throws ServiceException if was unable to get all categories
     */
    public void setAllCategoriesInRequestAttribute(HttpServletRequest req) throws ServiceException {
        List<Category> cats = categoryService.getAll();
        log.debug("retrieved a list of all categories, list size: {}", cats.size());
        req.setAttribute("categories", cats);
        log.debug("set categories list as a request attribute 'categories'");
    }

}
