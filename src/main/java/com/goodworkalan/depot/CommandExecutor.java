package com.goodworkalan.depot;


public class CommandExecutor extends Interpreter {
    private final Command command;

    private final String code;

    private final String parameters;
    
    private Interpreter nextInterpreter;
    
    public CommandExecutor(Command command, String code, String parameters) {
        this.command = command;
        this.code = code;
        this.parameters = parameters;
    }

    @Override
    public void execute(DepotSession session, Response response) {
        nextInterpreter = command.execute(code, parameters, session, response);
    }

    @Override
    public Interpreter nextInterpreter() {
        return nextInterpreter;
    }
}
