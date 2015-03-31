package com.hamstercoders.server.commands;

import java.util.HashMap;

/**
 * Created by Администратор on 3/25/15.
 */
public class CommandFactory {
    private static final CommandFactory INSTANCE = new CommandFactory();
    private HashMap<String, Command> commands;

    private CommandFactory() {
        commands = new HashMap<String, Command>();
        initCommands();
    }

    private void initCommands() {
        commands.put("status", new StatusCommand());
        commands.put("redirect", new RedirectCommand());
        commands.put("hello", new HelloCommand());
    }

    public Command getCommand(String reqCommand) {
        Command command = commands.get(reqCommand);
        if (command == null) {
            command = new NoCommand();
        }
        return command;
    }

    public static CommandFactory getInstance() {
        return INSTANCE;
    }





}
