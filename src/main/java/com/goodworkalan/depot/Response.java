package com.goodworkalan.depot;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.goodworkalan.manifold.Sender;
import com.goodworkalan.manifold.Wrapper;

public class Response
{
    private final Sender sender;
    
    public Response(Sender sender)
    {
        this.sender = sender;
    }
    
    public void sendLine(String line)
    {
        send(line + "\r\n");
    }
    
    public void send(String data)
    {
        sender.send(Charset.forName("UTF-8").encode(data));
    }

    public void send(ByteBuffer data)
    {
        sender.send(data);
    }
    
    public void send(byte[] data)
    {
        send(ByteBuffer.wrap(data));
    }
    
    public void ok(String code, String message)
    {
        sendLine(code + " OK " + message);
    }

    public void no(String code, String message)
    {
        sendLine(code + " NO " + message);
    }

    public void bad(String code, String message)
    {
        sendLine(code + " BAD " + message);
    }
    
    public void setWrapper(Wrapper wrapper)
    {
        sender.setWrapper(wrapper);
    }
    
    public void close()
    {
        sender.close();
    }
}
