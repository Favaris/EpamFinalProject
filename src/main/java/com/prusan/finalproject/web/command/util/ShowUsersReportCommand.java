package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.service.UserService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.PaginationAttributesHandler;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShowUsersReportCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        UserService us = ServiceFactory.getInstance().getUserService();

        String orderBy = handler.getOrderByFromParameters(req, "userLogin");
        String countLessThan = handler.getcountLessThanFromParameters(req);
        String countBiggerThan = handler.getcountBiggerThanFromParameters(req);
        String searchBy = handler.getSearchByFromParameters(req);

        try {
            List<User> usersList = us.getWithRoleUser(0, Integer.MAX_VALUE, orderBy, countLessThan, countBiggerThan, searchBy);

            req.setAttribute("usersList", usersList);
            log.debug("set up all needed request params");
            return Chain.createForward(Pages.USERS_REPORT_JSP);
        } catch (ServiceException e) {
            log.error("error while trying to download all users with role='user'", e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }
    }
}
