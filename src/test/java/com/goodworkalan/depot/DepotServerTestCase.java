package com.goodworkalan.depot;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.concurrent.Executors;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.testng.annotations.Test;

import com.goodworkalan.manifold.Manifold;

// TODO Move to different package for black box testing.
public class DepotServerTestCase
{
    private void list(Folder parent) throws MessagingException
    {
        for (Folder folder : parent.list())
        {
            if ((folder.getType() & Folder.HOLDS_FOLDERS) == Folder.HOLDS_FOLDERS)
            {
                System.out.println(folder.getFullName());
                list(folder);
            }
        }
    }

    @Test
    public void send() throws Exception
    {
        System.getProperty("javax.net.ssl.trustStore", "client.jks");
        
        KeyStore ks = KeyStore.getInstance("JKS");

        char[] passphrase = "password".toCharArray();

        ks.load(new FileInputStream("localhost.jks"), passphrase);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
        
        DepotSessionFactoryBuilder newDepotSessionFactory = new DepotSessionFactoryBuilder();
        newDepotSessionFactory.setSslContext(sslContext);
        
        Manifold manifold = new Manifold(newDepotSessionFactory.newDepotSessionFactory(), Executors.newCachedThreadPool());

        manifold.start();
        manifold.waitForStartup();
        
        Properties props = new Properties();
        props.setProperty("mail.imap.starttls.enable", "true");
        props.setProperty("mail.imap.port", "8143");
        Session session = Session.getInstance(props, null);
        session.setDebug(true);
        Store store = session.getStore("imap");
        store.connect("localhost", "alan", "password");
        Folder defaultFolder = store.getDefaultFolder();
        list(defaultFolder);
        System.out.println(defaultFolder.getSeparator());
       
        System.out.println(defaultFolder.getFullName());
        if (store.isConnected())
        {
            store.close();
        }
        
        props = new Properties();
        props.setProperty("mail.imap.auth.plain.disable", "true");
        props.setProperty("mail.imap.sasl.enable", "true");
        props.setProperty("mail.imap.port", "8143");
        session = Session.getInstance(props, null);
        session.setDebug(true);
        store = session.getStore("imap");
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
        manifold.join();
        if (!store.isConnected())
        {
            System.out.println("As expected.");
        }
        
        props = new Properties();
        props.setProperty("mail.imap.starttls.enable", "true");
        props.setProperty("mail.imap.port", "8143");
    }
}
