package com.goodworkalan.depot;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.net.ssl.SSLContext;

public class DepotSessionFactoryBuilder {
    private final static String[] CAPABILITIES = new String[] {
            "LOGINDISABLED", "STARTTLS"
    };

    private final static String[] AUTHENTICATIONS = new String[] {
            "PLAIN", "CRAM-MD5", "DIGEST-MD5", "GSSAPI"
    };

    private final Set<String> capabilities = new TreeSet<String>(Arrays.asList(CAPABILITIES)); 
    
    private final Set<String> authentications = new TreeSet<String>(Arrays.asList(AUTHENTICATIONS));
    
    private SSLContext sslContext;

    public DepotSessionFactoryBuilder() {
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
        }
    }

    public Set<String> getCapabilities()
    {
        return Collections.unmodifiableSet(capabilities);
    }
    
    public void addCapabilities(String capability) {
        capabilities.add(capability);
    }

    public boolean removeCapabilities(String capability) {
        return capabilities.remove(capability);
    }

    public Set<String> getAuthentications() {
        return Collections.unmodifiableSet(authentications);
    }

    public void addAuthentication(String authentiation) {
        authentications.add(authentiation);
    }
    
    public boolean removeAuthentication(String authentiation) {
        return authentications.remove(authentiation);
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public void setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
    }

    public DepotSessionFactory newDepotSessionFactory() {
        Map<String, Command> commands = new HashMap<String, Command>();

        commands.put("CAPABILITY", new Capability(Arrays.asList(CAPABILITIES), Arrays.asList(AUTHENTICATIONS)));
        commands.put("AUTHENTICATE", new Authenticate());
        commands.put("NOOP", new Noop());
        commands.put("LOGOUT", new Logout());
        commands.put("STARTTLS", new StartTLS(sslContext));
        commands.put("LIST", new List());
        
        return new DepotSessionFactory(new CommandInterpreterFactory(commands));
    }
}
