package com.goodworkalan.depot;

import java.nio.ByteBuffer;

// TODO Document.
public class Accept extends Interpreter
{
    // TODO Document.
    @Override
    public boolean read(ByteBuffer data) throws Bad
    {
        return true;
    }

    // TODO Document.
    @Override
    public void execute(DepotSession session, Response response)
    {
        response.sendLine("* OK IMAP4rev1 server ready");
        setNextInterpreter(session.newCommandInterpreter());
    }
}
