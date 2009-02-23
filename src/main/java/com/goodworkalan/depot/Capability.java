package com.goodworkalan.depot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Capability extends Command
{
    private final List<String> capabilities;
    
    private final List<String> authentications;
    
    public Capability(Collection<String> capabilities, Collection<String> authentications)
    {
        this.capabilities = new ArrayList<String>(capabilities);
        this.authentications = new ArrayList<String>(authentications);
    }
    
    public Interpreter execute(String code, String parameters, DepotSession session, Response response)
    {
        StringBuilder newString = new StringBuilder("* CAPABILITY IMAP4rev1");
        for (String capability : capabilities)
        {
            newString.append(" ").append(capability);
        }
        for (String authentication : authentications)
        {
            newString.append(" AUTH=").append(authentication);
        }
        response.sendLine(newString.toString());
        response.ok(code, "CAPABILITY completed");
        return session.newCommandInterpreter();
    }
}
