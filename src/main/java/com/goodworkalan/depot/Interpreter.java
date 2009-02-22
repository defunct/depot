package com.goodworkalan.depot;

import java.nio.ByteBuffer;

public class Interpreter
{
    public boolean read(ByteBuffer data) throws Bad
    {
        return false;
    }
    
    public Interpreter nextInterpreter()
    {
        return new CommandInterpreter();
    }
    
    public void execute(DepotSession session, Response response) throws Bad
    {
    }
}
