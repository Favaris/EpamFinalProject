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
    private static final Logger log = LogManager.getLogger(CommandContainer.class);

    static {
        commands = new HashMap<>();
        commands.put("signIn", new SignInCommand());
        commands.put("signUp", new SignUpCommand());
        commands.put("signOut", new SignOutCommand());
        commands.put("showActivitiesPage", new PrepareForShowingAllActivitiesCommand());
        commands.put("downloadAllActivities", new DownloadAllActivitiesCommand());
        commands.put("requestActivityAddition", new RequestActivityAdditionCommand());
        commands.put("showUsersRequests", new DownloadUsersRequestsCommand());
        commands.put("acceptRequest", new AcceptRequestCommand());
        commands.put("downloadUsersActivities", new DownloadUsersActivitiesCommand());
        commands.put("denyRequest", new DenyRequestCommand());
        commands.put("showAllUsers", new DownloadAllUsersCommand());
        commands.put("showEditUserPage", new PrepareForUserEditingCommand());
        commands.put("updateUser", new UpdateUserCommand());
        commands.put("showActivityEditPage", new PrepareForActivityEditingCommand());
        commands.put("updateActivity", new UpdateActivityCommand());
        commands.put("deleteActivity", new DeleteActivityCommand());
        commands.put("showActivityAddPage", new PrepareForActivityAdditionCommand());
        commands.put("addActivity", new AddActivityCommand());
        commands.put("addCategory", new AddCategoryCommand());
        commands.put("showCategoriesPage", new DownloadAllCategoriesCommand());
        commands.put("updateCategory", new UpdateCategoryCommand());
        commands.put("requestActivityAbandonment", new RequestActivityAbandonmentCommand());
        commands.put("cancelRequest", new CancelRequestCommand());
        commands.put("updateSpentTime", new UpdateTimeSpentCommand());
        commands.put("removeUserActivity", new DeleteUserActivityCommand());
        commands.put("showAddActivityForUserPage", new PrepareForAdditionActivitiesForUserCommand());
        commands.put("addUserActivity", new AddUserActivityCommand());
        commands.put("showUsersReport", new GetUserReportCommand());
        commands.put("deleteCategory", new DeleteCategoryCommand());
        commands.put("downloadActivitiesForUser", new DownloadActivitiesForUserCommand());
    }

    public static Command getCommand(String command) {
        Command c = commands.get(command);
        log.debug("returned a command {}", c);
        return commands.get(command);
    }
}
