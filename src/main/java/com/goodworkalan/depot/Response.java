package com.goodworkalan.depot;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

public class Response
{
    private final List<ByteBuffer> out;
    
    private boolean closed;
    
    public Response(List<ByteBuffer> out)
    {
        this.out = out;
    }
    
    public void sendLine(String line)
    {
        send(line + "\r\n");
    }
    
    public void send(String data)
    {
        out.add(Charset.forName("UTF-8").encode(data));
    }

    public void send(ByteBuffer data)
    {
        out.add(data);
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
    
    public void close()
    {
        closed = true;
    }

    public boolean isClosed()
    {
        return closed;
    }
}
