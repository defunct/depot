package com.goodworkalan.depot;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.testng.annotations.Test;

import com.goodworkalan.manifold.Manifold;

public class DepotServerTestCase
{
    @Test
    public void send() throws MessagingException, InterruptedException, IOException
    {
        final Manifold manifold = new Manifold(new DepotSessionFactory(), Executors.newCachedThreadPool());
        
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    manifold.bind();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        
        manifold.waitForStartup();
        
        Properties props = new Properties();
        props.setProperty("mail.imap.auth.plain.disable", "true");
        props.setProperty("mail.imap.sasl.enable", "true");
        props.setProperty("mail.imap.port", "8143");
        Session session = Session.getInstance(props, null);
        session.setDebug(true);
        Store store = session.getStore("imap");
        store.connect("localhost", "alan", "password");
        if (store.isConnected())
        {
            store.close();
        }
        session = Session.getInstance(props, null);
        session.setDebug(true);
        store = session.getStore("imap");
        store.connect("localhost", "alan", "password");
        manifold.shutdown();
        thread.join();
        if (!store.isConnected())
        {
            System.out.println("As expected.");
        }
    }
}
