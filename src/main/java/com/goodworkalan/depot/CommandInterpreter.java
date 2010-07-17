package com.goodworkalan.depot;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandInterpreter extends LineInterpreter {
    private final static Pattern isCommand = Pattern.compile("([^ )({%*\"\\\\\\]]+)\\s+(\\w+)(.*)$");
    
    private final Map<String, Command> commands;
    
    private CommandExecutor commandExecutor;

    public CommandInterpreter(Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(DepotSession session, Response response) throws Bad {
        Matcher matcher = isCommand.matcher(getLine());
        if (!matcher.lookingAt()) {
            throw new Bad("*", "Malformed command");
        }
        String code = matcher.group(1);
        String name = matcher.group(2);
        String parameters = matcher.group(3).trim();
        commandExecutor = new CommandExecutor(commands.get(name), code, parameters);
        commandExecutor.execute(session, response);
    }
    
    @Override
    public Interpreter nextInterpreter() {
        return commandExecutor.nextInterpreter();
    }
}
