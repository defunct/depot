package com.goodworkalan.depot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandInterpreter extends LineInterpreter
{
    private final static Pattern isCommand = Pattern.compile("([^ )({%*\"\\\\\\]]+)\\s+(\\w+)(.*)$");
    
    private final static Map<String, Command> commands = new HashMap<String, Command>();
    
    private CommandExecutor commandExecutor;
    
    static
    {
        commands.put("CAPABILITY", new Capability());
        commands.put("AUTHENTICATE", new Authenticate());
        commands.put("NOOP", new Noop());
        commands.put("LOGOUT", new Logout());
    }
    
    public void read(String data) throws Bad
    {

    }
    
    @Override
    public void execute(DepotSession session, Response response) throws Bad
    {
        Matcher matcher = isCommand.matcher(getLine());
        if (!matcher.lookingAt())
        {
            throw new Bad("*", "Malformed command");
        }
        String code = matcher.group(1);
        String name = matcher.group(2);
        String parameters = matcher.group(3).trim();
        commandExecutor = new CommandExecutor(commands.get(name), code, parameters);
        commandExecutor.execute(session, response);
    }
    
    @Override
    public Interpreter nextInterpreter()
    {
        return commandExecutor.nextInterpreter();
    }
}
