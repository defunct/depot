package com.goodworkalan.depot;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import com.goodworkalan.manifold.ssl.SslHandshakeWrapper;

public class StartTLS extends Command
{
    private final SSLContext sslContext;
    
    public StartTLS(SSLContext sslContext)
    {
        this.sslContext = sslContext;
    }

    @Override
    public Interpreter execute(String code, String parameters, DepotSession session, Response response)
    {
        SSLEngine sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(false);
        response.ok(code, "Begin TLS negotiation now");
        response.setWrapper(new SslHandshakeWrapper(sslEngine));
        return session.newCommandInterpreter();
    }
}
