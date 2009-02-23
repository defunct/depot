package com.goodworkalan.depot;

public class Noop extends Command
{
    @Override
    public Interpreter execute(String code, String parameters, DepotSession session, Response response)
    {
        response.ok(code, "completed");
        return session.newCommandInterpreter();
    }
}
