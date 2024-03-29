package com.prusan.finalproject.db.util;

import com.prusan.finalproject.db.dao.ActivityDAO;
import com.prusan.finalproject.db.dao.CategoryDAO;
import com.prusan.finalproject.db.dao.UserActivityDAO;
import com.prusan.finalproject.db.dao.UserDAO;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.CategoryService;
import com.prusan.finalproject.db.service.UserActivityService;
import com.prusan.finalproject.db.service.UserService;

/**
 * Abstract factory. Contains methods that are used to get different service and dao implementors.
 */
public interface ServiceFactory {
    UserService getUserService();
    ActivityService getActivityService();
    CategoryService getCategoryService();
    UserActivityService getUserActivityService();

    UserDAO getUserDAO();
    ActivityDAO getActivityDAO();
    CategoryDAO getCategoryDAO();
    UserActivityDAO getUserActivityDAO();

    static ServiceFactory getInstance() {
        return new ServiceFactoryImpl();
    }
}
