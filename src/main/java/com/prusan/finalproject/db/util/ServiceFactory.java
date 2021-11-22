package com.prusan.finalproject.db.util;

import com.prusan.finalproject.db.dao.ActivityDAO;
import com.prusan.finalproject.db.dao.CategoryDAO;
import com.prusan.finalproject.db.dao.UserDAO;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.CategoryService;
import com.prusan.finalproject.db.service.UserService;

/**
 * Factory method interface. Contains methods that are used to get different service and dao implementors.
 */
public interface ServiceFactory {
    UserService getUserService();
    ActivityService getActivityService();
    CategoryService getCategoryService();

    UserDAO getUserDAO();
    ActivityDAO getActivityDAO();
    CategoryDAO getCategoryDAO();

    static ServiceFactory getInstance() {
        return new ServiceFactoryImpl();
    }
}
