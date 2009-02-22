package com.goodworkalan.depot;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.manifold.Sender;
import com.goodworkalan.manifold.Session;

public class DepotSession implements Session
{
    private final Authenticator authenticator;
    
    private Interpreter interpreter;
    
    private String authorizationId;
    
    private DataWrapper dataWrapper = new DataWrapper(); 
    
    public DepotSession(Authenticator authenticator)
    {
        this.authenticator = authenticator;
    }
    
    public void setDataWrapper(DataWrapper dataWrapper)
    {
        this.dataWrapper = dataWrapper;
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
        List<ByteBuffer> out = new ArrayList<ByteBuffer>();
        Response response = new Response(out);
        try
        {
            while (interpreter.read(data))
            {
                interpreter.execute(this, response);
                interpreter = interpreter.nextInterpreter();
            }
            if (!out.isEmpty())
            {
                List<ByteBuffer> secure = new ArrayList<ByteBuffer>();
                for (ByteBuffer unwrapped : out)
                {
                    ByteBuffer wrapped = dataWrapper.wrap(unwrapped);
                    if (wrapped == null)
                    {
                        sender.close();
                    }
                    secure.add(wrapped);
                }
                sender.send(secure);
            }
            if (response.isClosed())
            {
                sender.close();
            }
        }
        catch (Bad bad)
        {
            response.send(bad.getMessage());
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
