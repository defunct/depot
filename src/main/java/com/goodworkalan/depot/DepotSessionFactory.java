package com.goodworkalan.depot;

import java.net.InetAddress;

import com.goodworkalan.manifold.Session;
import com.goodworkalan.manifold.SessionFactory;

public class DepotSessionFactory implements SessionFactory
{
    public Session accept(InetAddress remote)
    {
        return new DepotSession(new Authenticator());
    }
}
