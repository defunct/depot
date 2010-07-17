package com.goodworkalan.depot;

import java.nio.ByteBuffer;

public class Terminate extends Interpreter {
    @Override
    public boolean read(ByteBuffer data) throws Bad {
        return true;
    }

    @Override
    public void execute(DepotSession session, Response response) throws Bad {
        response.sendLine("* BYE Shutting down");
    }

    @Override
    public Interpreter nextInterpreter() {
        return new Interpreter();
    }
}
