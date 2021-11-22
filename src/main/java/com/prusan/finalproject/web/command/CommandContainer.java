package com.prusan.finalproject.web.command;

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
        commands.put("downloadActivities", new DownloadAllActivitiesCommand());
        commands.put("addUserActivityRequest", new AddUserActivityRequestCommand());
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
    }

    public static Command getCommand(String command) {
        Command c = commands.get(command);
        log.debug("returned a command {}", c);
        return commands.get(command);
    }
}
