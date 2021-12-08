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

    public interface CommandNames {
        String ACCEPT_REQUEST = "acceptRequest";
        String ADD_ACTIVITY = "addActivity";
        String ADD_USER_ACTIVITY = "addUserActivity";
        String DELETE_ACTIVITY = "deleteActivity";
        String REMOVE_USER_ACTIVITY = "removeUserActivity";
        String DENY_REQUEST = "denyRequest";
        String UPDATE_SPENT_TIME = "updateSpentTime";
        String UPDATE_ACTIVITY = "updateActivity";
        String ADD_CATEGORY = "addCategory";
        String UPDATE_CATEGORY = "updateCategory";
        String DELETE_CATEGORY = "deleteCategory";
        String ADD_USER = "addUser";
        String UPDATE_USER = "updateUser";
        String SIGN_OUT = "signOut";
        String SIGN_UP = "signUp";
        String SIGN_IN = "signIn";
        String REQUEST_ACTIVITY_ADDITION = "requestActivityAddition";
        String REQUEST_ACTIVITY_ABANDONMENT = "requestActivityAbandonment";
        String SHOW_USERS_REPORT = "showUsersReport";
        String CANCEL_REQUEST = "cancelRequest";
        String MANAGE_USERS_ACTIVITIES = "manageUsersActivities";
        String SHOW_ACTIVITIES_PAGE = "showActivitiesPage";
        String SHOW_USERS_REQUESTS = "showUsersRequests";
        String SHOW_RUNNING_ACTIVITIES = "showRunningActivities";
        String SHOW_DETAILED_USER_INFO = "showDetailedUserInfo";
        String SHOW_CATEGORIES_PAGE = "showCategoriesPage";
        String SHOW_ALL_USERS = "showAllUsers";
        String SHOW_ADD_ACTIVITIES_FOR_USER_PAGE = "showAddActivitiesForUserPage";
        String SHOW_ACTIVITY_EDIT_PAGE = "showActivityEditPage";
        String SHOW_ACTIVITY_ADD_PAGE = "showActivityAddPage";
    }


    static {
        commands = new HashMap<>();
        commands.put(CommandNames.ACCEPT_REQUEST, new AcceptRequestCommand());
        commands.put(CommandNames.ADD_ACTIVITY, new AddActivityCommand());
        commands.put(CommandNames.ADD_USER_ACTIVITY, new AddUserActivityCommand());
        commands.put(CommandNames.DELETE_ACTIVITY, new DeleteActivityCommand());
        commands.put(CommandNames.REMOVE_USER_ACTIVITY, new DeleteUserActivityCommand());
        commands.put(CommandNames.UPDATE_SPENT_TIME, new UpdateTimeSpentCommand());
        commands.put(CommandNames.UPDATE_ACTIVITY, new UpdateActivityCommand());

        commands.put(CommandNames.ADD_CATEGORY, new AddCategoryCommand());
        commands.put(CommandNames.UPDATE_CATEGORY, new UpdateCategoryCommand());
        commands.put(CommandNames.DELETE_CATEGORY, new DeleteCategoryCommand());

        commands.put(CommandNames.ADD_USER, new AddUserCommand());
        commands.put(CommandNames.CANCEL_REQUEST, new CancelRequestCommand());
        commands.put(CommandNames.DENY_REQUEST, new DenyRequestCommand());
        commands.put(CommandNames.REQUEST_ACTIVITY_ABANDONMENT, new RequestActivityAbandonmentCommand());
        commands.put(CommandNames.REQUEST_ACTIVITY_ADDITION, new RequestActivityAdditionCommand());
        commands.put(CommandNames.SIGN_IN, new SignInCommand());
        commands.put(CommandNames.SIGN_UP, new SignUpCommand());
        commands.put(CommandNames.SIGN_OUT, new SignOutCommand());
        commands.put(CommandNames.UPDATE_USER, new UpdateUserCommand());

        commands.put(CommandNames.MANAGE_USERS_ACTIVITIES, new ManageUsersActivitiesCommand());
        commands.put(CommandNames.SHOW_ACTIVITIES_PAGE, new ShowActivitiesCommand());
        commands.put(CommandNames.SHOW_ACTIVITY_ADD_PAGE, new ShowActivityAddPageCommand());
        commands.put(CommandNames.SHOW_ACTIVITY_EDIT_PAGE, new ShowActivityEditPageCommand());
        commands.put(CommandNames.SHOW_ADD_ACTIVITIES_FOR_USER_PAGE, new ShowAddActivitiesForUserPageCommand());
        commands.put(CommandNames.SHOW_ALL_USERS, new ShowAllUsersCommand());
        commands.put(CommandNames.SHOW_CATEGORIES_PAGE, new ShowCategoriesCommand());
        commands.put(CommandNames.SHOW_DETAILED_USER_INFO, new ShowDetailedUserInfoCommand());
        commands.put(CommandNames.SHOW_RUNNING_ACTIVITIES, new ShowRunningActivitiesCommand());
        commands.put(CommandNames.SHOW_USERS_REQUESTS, new ShowRequestsCommand());
        commands.put(CommandNames.SHOW_USERS_REPORT, new ShowUsersReportCommand());
    }

    public static Command getCommand(String command) {
        Command c = commands.get(command);
        log.debug("returned a command {}", c);
        return commands.get(command);
    }
}
