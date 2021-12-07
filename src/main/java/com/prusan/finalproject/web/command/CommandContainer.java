package com.prusan.finalproject.web.command;

import com.prusan.finalproject.web.command.activity.*;
import com.prusan.finalproject.web.command.category.AddCategoryCommand;
import com.prusan.finalproject.web.command.category.DeleteCategoryCommand;
import com.prusan.finalproject.web.command.category.UpdateCategoryCommand;
import com.prusan.finalproject.web.command.user.*;
import com.prusan.finalproject.web.command.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple container for all types of commands.
 */
public class CommandContainer {
    private static final Map<String, Command> commands;
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    static {
        commands = new HashMap<>();
        commands.put("acceptRequest", new AcceptRequestCommand());
        commands.put("addActivity", new AddActivityCommand());
        commands.put("addUserActivity", new AddUserActivityCommand());
        commands.put("deleteActivity", new DeleteActivityCommand());
        commands.put("removeUserActivity", new DeleteUserActivityCommand());
        commands.put("denyRequest", new DenyRequestCommand());
        commands.put("updateActivity", new UpdateActivityCommand());
        commands.put("updateSpentTime", new UpdateTimeSpentCommand());

        commands.put("addCategory", new AddCategoryCommand());
        commands.put("updateCategory", new UpdateCategoryCommand());
        commands.put("deleteCategory", new DeleteCategoryCommand());

        commands.put("addUser", new AddUserCommand());
        commands.put("cancelRequest", new CancelRequestCommand());
        commands.put("showUsersReport", new GetUserReportCommand());
        commands.put("requestActivityAbandonment", new RequestActivityAbandonmentCommand());
        commands.put("requestActivityAddition", new RequestActivityAdditionCommand());
        commands.put("signIn", new SignInCommand());
        commands.put("signUp", new SignUpCommand());
        commands.put("signOut", new SignOutCommand());
        commands.put("updateUser", new UpdateUserCommand());

        commands.put("manageUsersActivities", new ManageUsersActivitiesCommand());
        commands.put("showActivitiesPage", new ShowActivitiesCommand());
        commands.put("showActivityAddPage", new ShowActivityAddPageCommand());
        commands.put("showActivityEditPage", new ShowActivityEditPageCommand());
        commands.put("showAddActivitiesForUserPage", new ShowAddActivitiesForUserPageCommand());
        commands.put("showAllUsers", new ShowAllUsersCommand());
        commands.put("showCategoriesPage", new ShowCategoriesCommand());
        commands.put("showDetailedUserInfo", new ShowDetailedUserInfoCommand());
        commands.put("showRunningActivities", new ShowRunningActivitiesCommand());
        commands.put("showUsersRequests", new ShowRequestsCommand());
    }

    public static Command getCommand(String command) {
        Command c = commands.get(command);
        log.debug("returned a command {}", c);
        return commands.get(command);
    }
}
