package com.goodworkalan.depot;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import com.goodworkalan.manifold.Manifold;

public class Depot
{
    public static void main(String[] args) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, KeyManagementException, UnrecoverableKeyException
    {
        KeyStore ks = KeyStore.getInstance("JKS");

        char[] passphrase = "password".toCharArray();

        ks.load(new FileInputStream("localhost.jks"), passphrase);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
        
        DepotSessionFactoryBuilder newDepotSessionFactory = new DepotSessionFactoryBuilder();
        newDepotSessionFactory.setSslContext(sslContext);
        
        new Manifold(newDepotSessionFactory.newDepotSessionFactory(), Executors.newCachedThreadPool()).start();
    }
}
