package com.goodworkalan.depot;

import java.net.InetAddress;

import com.goodworkalan.manifold.Session;
import com.goodworkalan.manifold.SessionFactory;

public class DepotSessionFactory implements SessionFactory
{
    private CommandInterpreterFactory commandInterpreterFactory;
    
    public DepotSessionFactory(CommandInterpreterFactory commandInterpreterFactory)
    {
        this.commandInterpreterFactory = commandInterpreterFactory;
    }

    public Session accept(InetAddress remote)
    {
        return new DepotSession(commandInterpreterFactory, new Authenticator());
    }
}
