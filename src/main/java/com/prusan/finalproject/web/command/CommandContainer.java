package com.prusan.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class CommandContainer {
    private static final Map<String, Command> commands;
    private static final Logger log = LogManager.getLogger(CommandContainer.class);

    static {
        commands = new HashMap<>();
        commands.put("sign_in", new SignInCommand());
        commands.put("sign_up", new SignUpCommand());
    }

    public static Command getCommand(String command) {
        Command c = commands.get(command);
        log.debug("returned a command {}", c);
        return commands.get(command);
    }
}
