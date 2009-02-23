package com.goodworkalan.depot;

import java.nio.ByteBuffer;

public class Interpreter
{
    private Interpreter nextInterpreter;
    
    public boolean read(ByteBuffer data) throws Bad
    {
        return false;
    }
    
    public void setNextInterpreter(Interpreter nextInterpreter)
    {
        this.nextInterpreter = nextInterpreter;
    }
    
    public Interpreter nextInterpreter()
    {
        return nextInterpreter;
    }
    
    public void execute(DepotSession session, Response response) throws Bad
    {
    }
}
