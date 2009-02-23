package com.goodworkalan.depot;

public class Logout extends Command
{
    @Override
    public Interpreter execute(String code, String parameters, DepotSession session, Response response)
    {
        response.sendLine("* BYE IMAP4rev1 Server logging out");
        response.ok(code, " LOGOUT Completed");
        response.close();
        return session.newCommandInterpreter();
    }
}
