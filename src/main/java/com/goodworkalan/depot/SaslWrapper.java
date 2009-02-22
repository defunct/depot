package com.goodworkalan.depot;

import java.nio.ByteBuffer;

import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

public class SaslWrapper extends DataWrapper
{
    private final SaslServer saslServer;
    
    private byte[] temporary = new byte[1024];
    
    public SaslWrapper(SaslServer saslServer)
    {
        this.saslServer = saslServer;
    }
    
    @Override
    public ByteBuffer wrap(ByteBuffer unwrapped)
    {
        unwrapped.clear();
        int size = unwrapped.remaining();
        if (size < temporary.length)
        {
            temporary = new byte[size];
        }
        unwrapped.get(temporary);
        try
        {
            return ByteBuffer.wrap(saslServer.wrap(temporary, 0, size));
        }
        catch (SaslException e)
        {
            return null;
        }
    }
    
    @Override
    public ByteBuffer unwrap(ByteBuffer wrapped)
    {
        wrapped.clear();
        int size = wrapped.remaining();
        if (size < temporary.length)
        {
            temporary = new byte[size];
        }
        wrapped.get(temporary);
        try
        {
            return ByteBuffer.wrap(saslServer.unwrap(temporary, 0, size));
        }
        catch (SaslException e)
        {
            return null;
        }
    }
}
