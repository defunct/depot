package com.goodworkalan.depot;

import java.nio.ByteBuffer;


public class Accept extends Interpreter
{
    @Override
    public boolean read(ByteBuffer data) throws Bad
    {
        return true;
    }

    @Override
    public void execute(DepotSession session, Response response)
    {
        response.sendLine("* OK IMAP4rev1 server ready");
    }
}
