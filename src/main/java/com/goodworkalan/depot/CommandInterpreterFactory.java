package com.goodworkalan.depot;

import java.util.Map;

public class CommandInterpreterFactory
{
    private final Map<String, Command> commands;
    
    public CommandInterpreterFactory(Map<String, Command> commands)
    {
        this.commands = commands;
    }
    
    public Interpreter newCommandInterperter()
    {
        return new CommandInterpreter(commands);
    }
}
