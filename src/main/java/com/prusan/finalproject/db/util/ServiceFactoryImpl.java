package com.prusan.finalproject.db.util;

import com.prusan.finalproject.db.dao.ActivityDAO;
import com.prusan.finalproject.db.dao.CategoryDAO;
import com.prusan.finalproject.db.dao.UserDAO;
import com.prusan.finalproject.db.dao.implementor.ActivityDAOImpl;
import com.prusan.finalproject.db.dao.implementor.CategoryDAOImpl;
import com.prusan.finalproject.db.dao.implementor.UserDAOImpl;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.UserService;
import com.prusan.finalproject.db.service.implementor.ActivityServiceImpl;
import com.prusan.finalproject.db.service.implementor.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.internal.LogManagerStatus;


/**
 * Basic implementor for ServiceFactory interface. Uses classes from db.dao.implementor and db.service.implementor packages.
 */
public class ServiceFactoryImpl implements ServiceFactory {
    private static final Logger log = LogManager.getLogger(ServiceFactoryImpl.class);

    @Override
    public UserService getUserService() {
        UserServiceImpl us = new UserServiceImpl();
        us.setUserDAO(getUserDAO());
        log.debug("returned a UserService instance: {}", us);
        return us;
    }

    @Override
    public ActivityService getActivityService() {
        ActivityServiceImpl as = new ActivityServiceImpl();
        as.setActivityDAO(getActivityDAO());
        as.setCategoryDAO(getCategoryDAO());
        as.setUserDAO(getUserDAO());
        log.debug("returned an ActivityService instance: {}", as);
        return as;
    }

    @Override
    public UserDAO getUserDAO() {
        return new UserDAOImpl();
    }

    @Override
    public ActivityDAO getActivityDAO() {
        return new ActivityDAOImpl();
    }

    @Override
    public CategoryDAO getCategoryDAO() {
        return new CategoryDAOImpl();
    }
}
