package com.prusan.finalproject.db.util;

import com.prusan.finalproject.db.dao.ActivityDAO;
import com.prusan.finalproject.db.dao.CategoryDAO;
import com.prusan.finalproject.db.dao.UserDAO;
import com.prusan.finalproject.db.dao.implementor.ActivityDAOImpl;
import com.prusan.finalproject.db.dao.implementor.CategoryDAOImpl;
import com.prusan.finalproject.db.dao.implementor.UserDAOImpl;
import com.prusan.finalproject.db.service.UserService;
import com.prusan.finalproject.db.service.implementor.UserServiceImpl;

/**
 * Basic implementor for ServiceFactory interface. Uses classes from db.dao.implementor and db.service.implementor packages.
 */
public class ServiceFactoryImpl implements ServiceFactory {
    @Override
    public UserService getUserService() {
        UserServiceImpl us = new UserServiceImpl();
        us.setUserDAO(getUserDAO());
        return us;
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
