package com.goodworkalan.depot;

import java.nio.ByteBuffer;

import com.goodworkalan.manifold.Sender;
import com.goodworkalan.manifold.Session;


public class DepotSession implements Session
{
    private final Authenticator authenticator;
    
    private Interpreter interpreter;
    
    private String authorizationId;
    
    private CommandInterpreterFactory commandInterpreterFactory;
    
    public DepotSession(CommandInterpreterFactory commandInterpreterFactory, Authenticator authenticator)
    {
        this.commandInterpreterFactory = commandInterpreterFactory;
        this.authenticator = authenticator;
    }
    
    public Interpreter newCommandInterpreter()
    {
        return commandInterpreterFactory.newCommandInterperter();
    }
    
    public void setAuthorizationId(String authorizationId)
    {
        this.authorizationId = authorizationId;
    }
    
    public String getAuthorizationId()
    {
        return authorizationId;
    }
    
    public Authenticator getAuthenticator()
    {
        return authenticator;
    }
    
    public void accepted(Sender sender)
    {
        interpreter = new Accept();
        read(ByteBuffer.allocate(0), sender);
    }
    
    public void read(ByteBuffer data, Sender sender)
    {
        Response response = new Response(sender);
        try
        {
            while (interpreter.read(data))
            {
                interpreter.execute(this, response);
                interpreter = interpreter.nextInterpreter();
            }
        }
        catch (Bad bad)
        {
            response.send(bad.getMessage());
            interpreter = newCommandInterpreter();
        }
    }
    
    public void terminate(Sender sender)
    {
        interpreter = new Terminate();
        read(ByteBuffer.allocate(0), sender);
    }

    public void close()
    {
    }
}
