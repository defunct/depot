package com.goodworkalan.depot;


public class Capability extends Command
{
    public Interpreter execute(String code, String parameters, DepotSession session, Response response)
    {
        response.sendLine("* CAPABILITY IMAP4rev1 STARTTLS AUTH=DIGEST-MD5 AUTH=GSSAPI AUTH=PLAIN LOGINDISABLED");
        response.ok(code, "CAPABILITY completed");
        return new CommandInterpreter();
    }
}
